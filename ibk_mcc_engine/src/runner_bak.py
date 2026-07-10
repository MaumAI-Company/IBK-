# MindsLab Inc. 
# Multi-Category Classifier Runner

import argparse, logging, shutil
from logging.handlers import RotatingFileHandler
from pathlib import Path
from datetime import datetime

import tensorflow as tf
from tqdm import tqdm
from omegaconf import OmegaConf
from tensorflow.python.client import device_lib

# engine modules
from trfms import find_architecture
from engine import MultiCategoryClassifier as MccEngine
from data_processor import MccDataProcessor
import component

logger = logging.getLogger(__name__)

# a config file containing constants about paths & file names
CONST_CFG = 'cfg/const.yml'
const = OmegaConf.load(CONST_CFG)


# 1. Load data 
# 2. Build input_fn
# 3. Build estimator/predictor
# 4. Run train/test/infer
# 5. Post process
class MccRunner(object):
    def __init__(self, cfg):
        self.cfg = cfg

        # instantiate essential objects with the given config
        architecture = find_architecture(cfg.model.architecture)
        self.data_processor = MccDataProcessor(cfg)
        self.engine = MccEngine(cfg, architecture)

        # for inference & serving. singleton pattern
        self.predictor = None   

    # Train
    def train(self):
        train_cfg = self.cfg.train

        input_fn, inputs = self.data_processor.build_train_input_fn(train_cfg)
        gpu_count = len([x.name for x in device_lib.list_local_devices() if x.device_type == 'GPU'])
        train_steps = ((len(inputs) * train_cfg.epochs) // (train_cfg.batch_size * gpu_count)) + 1
        estimator = self.engine.build_estimator(train_cfg, train_steps)

        estimator.train(input_fn=input_fn, max_steps=train_steps, hooks=[SaveProgressHook(self.cfg, train_steps // gpu_count)])

    # Batch inference with test data
    def test(self, test_data_file):
        dp = self.data_processor
        examples = dp.build_test_examples(test_data_file)

        results = self._infer(examples)
        dp.evaluate_result(results)

    # Inference with input texts. for infer & serve
    def infer(self, infer_texts, input_floats=None):
        dp = self.data_processor
        examples = dp.build_infer_examples(infer_texts, input_floats)

        results = self._infer(examples)

        categories = dp.label2idx_dict_by_category.keys()
        
        infer_results = []
        for _, _, _, top_preds_by_category in results:
            infer_result = []
            for category, top_preds in zip(categories, top_preds_by_category):
                predictions = [(dp.idx2label_by_category[category][pred_idx], pred_prob) 
                               for pred_idx, pred_prob in top_preds]
                infer_result.append([category, predictions])
            infer_results.append(infer_result)
        return infer_results

    # Used for both test & infer
    def _infer(self, inputs):
        cfg = self.cfg
        infer_cfg = cfg.infer

        dp = self.data_processor

        # Build predictor using infer_cfg
        if self.predictor is None:
            infer_fn = dp.build_infer_fn()
            self.predictor = self.engine.build_predictor(infer_cfg, infer_fn)

        batched_inputs = []
        batched_outputs = []
        for batch_beg in tqdm(range(0, len(inputs), infer_cfg.batch_size), desc='Processing inputs'):
            batch_end = batch_beg + infer_cfg.batch_size
            batched_input = inputs[batch_beg:batch_end] 

            features = dp.build_infer_features(batched_input)
            batched_output = self.predictor(features)

            batched_inputs.append(batched_input)
            batched_outputs.append(batched_output)

        results = dp.postprocess_batches(batched_inputs, batched_outputs)

        return results

    def test_batch(self):
        cfg = self.cfg
        test_result_file = Path(cfg.path.model_dir, cfg.const.test_result)

        if test_result_file.exists():
            test_result = OmegaConf.load(str(test_result_file))
        else:
            test_result = OmegaConf.create()
            test_result.results = []
            OmegaConf.save(test_result, str(test_result_file))

        scores_dict = self.test(cfg.infer.data)
        print(scores_dict.keys())
        print(scores_dict)
        for i in range(4):
            test_result.results.append({
                'category': 'dummy_' + str(i),
                'f1': 0.2 * i,
                'precision': 0.1 * i,
                'recall': 0.03 * i,
            })
        OmegaConf.save(test_result, str(test_result_file))


class SaveProgressHook(tf.train.SessionRunHook):
    def __init__(self, cfg, train_steps):
        self.cfg = cfg
        self.log_every = cfg.train.log_step_count_steps
        self.model_status_path = Path(cfg.const.models_dir, cfg.model_name, cfg.const.model_status)
        self.total_train_steps = train_steps

        if not self.model_status_path.exists():
            self.build_status()
            self.write_status()
        self.read_status()

    def build_status(self):
        self.status = OmegaConf.create()
        self.status.total_steps = self.total_train_steps
        self.status.cur_step = 0

    def write_status(self):
        OmegaConf.save(self.status, str(self.model_status_path))

    def read_status(self):
        self.status = OmegaConf.load(str(self.model_status_path))

    def end(self, run_context):
        self.write_status()

    def before_run(self, run_context):
        self.status.cur_step += 1

    def after_run(self, run_context, run_values):
        if self.status.cur_step % self.log_every == 0:
            self.write_status()


# define and read arguments especially for running, not modeling
def parse_args(argv=None):
    parser = argparse.ArgumentParser()
    # Arguments for running.
    parser.add_argument('--run', type=str, required=True, choices=['train', 'test', 'infer', 'serve'])
    parser.add_argument('--log_level', type=str, default='INFO', choices=['INFO', 'DEBUG'])

    # Configuration files for training
    parser.add_argument('--cfg', type=str, default='cfg/config.yml')
    parser.add_argument('--model_cfg', type=str, default='cfg/model.yml')

    # for test/inference. config files will be loaded from the model_path
    parser.add_argument('--model_path', type=str)
    parser.add_argument('--infer_text', type=str, dest='infer_texts', action='append')
    parser.add_argument('--infer_float', type=str, dest='infer_floats', action='append')

    args = parser.parse_args(argv)
    return args

def setup_logger(cfg):
    log_file_name = '{}_{}.log'.format(datetime.now().strftime('%y%m%d_%H%M%S'), cfg.args.run)
    log_file_path = Path(cfg.const.log_dir, cfg.model_name, log_file_name)
    log_file_path.parent.mkdir(parents=True, exist_ok=True)
    Path(const.serving_model_dir).mkdir(parents=True, exist_ok=True)

    logging.basicConfig(level=logging.DEBUG)
    formatter = logging.Formatter('[%(levelname)s|%(filename)s:%(asctime)s] %(message)s')

    # create file handler
    fh = RotatingFileHandler(str(log_file_path), maxBytes=2*1024*1024, backupCount=5)
    fh.setLevel(logging.DEBUG)
    fh.setFormatter(formatter)

    # create stderr handler
    ch = logging.StreamHandler()    # stderr by default
    ch.setLevel(cfg.args.log_level)
    ch.setFormatter(formatter)
    # because 'tensorflow' logger output log to stderr, setting additional stream handler is not necessary.

    handlers = [fh, ch]
    logging.getLogger('').handlers = handlers
    logging.getLogger('tensorflow').handlers = []

    logger.setLevel = cfg.args.log_level

# Setup configuration
def setup_config(args, update_cfg=None):
    cfg = None

    # Set model_dir_path
    if args.run == 'train':
        cfg = OmegaConf.load(args.cfg)
        if update_cfg:
            cfg = OmegaConf.merge(cfg, update_cfg)
        model_dir_path = Path(const.models_dir, cfg.model_name)
        model_dir_path.mkdir(parents=True, exist_ok=True)
    else:
        model_path = Path(args.model_path)
        model_dir_path = model_path if model_path.is_dir() else model_path.parent

    # paths for essential files under the model dir
    cfg_file = str(model_dir_path.joinpath(const.run_cfg))
    model_cfg_file = str(model_dir_path.joinpath(const.model_cfg))
    vocab_file = str(model_dir_path.joinpath(const.vocab_file))

    # Pack & store essential configurations into the model dir.
    if args.run == 'train':
        if cfg is None:
            shutil.copyfile(args.cfg, cfg_file)
        else:
            with open(cfg_file, 'w') as f:
                OmegaConf.save(cfg, f)
        shutil.copyfile(args.model_cfg, model_cfg_file)
        shutil.copyfile(cfg.train.vocab_file, vocab_file)

    # Load configurations from the model dir
    cfg = OmegaConf.load(cfg_file)
    cfg.model = OmegaConf.load(model_cfg_file)
    cfg.const = const
    cfg.args = vars(args)

    # Set a proper checkpoint path
    # train: latest > pretrained
    # infer: model_path > latest
    latest_ckpt = tf.train.latest_checkpoint(str(model_dir_path))
    if args.run == 'train':
        model_ckpt = cfg.train.pretrained_ckpt if latest_ckpt is None else latest_ckpt
    else:
        model_ckpt = args.model_path if tf.train.checkpoint_exists(args.model_path) else latest_ckpt

    cfg.path = {}
    cfg.path.model_dir = str(model_dir_path)
    cfg.path.vocab_file = vocab_file
    cfg.path.init_checkpoint = model_ckpt
    cfg.model_name = model_dir_path.name

    return cfg

def main_process(cfg):
    args = cfg.args

    # Initialize runner instance
    runner = MccRunner(cfg)

    # Run a specific phase. Currently restricted for running only one phase at a time.
    if args.run == 'train':
        runner.train()

    elif args.run == 'test':
        runner.test(cfg.infer.data)

    elif args.run == 'infer':
        results = runner.infer(args.infer_texts, args.infer_floats)
        print(results)
        for text, result in zip(args.infer_texts, results):
            print('[Input text]:', text)
            if args.infer_floats:
                print('[Input floats]:', args.infer_floats)
            print('[Predictions]:')
            for category, predictions in result:
                print('== {} =='.format(category))
                for idx, (prediction, probability) in enumerate(predictions):
                    print('# {}. {} ({:0.02f})'.format(idx+1, prediction, probability*100))
            print()

    print("Done running")


if __name__ == '__main__':
    args = parse_args()

    # Argument checking. Configs will be loaded in different ways depending on the run_phase.
    assert (args.run == 'train' and args.cfg != None and args.model_cfg != None)\
        or (args.run != 'train' and args.model_path != None)

    # Initial setup
    cfg = setup_config(args)
    setup_logger(cfg)
    main_process(cfg)
