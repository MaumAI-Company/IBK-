# MindsLab Inc. 
# Multi-Category Classifier

import json
import math
from pathlib import Path
from collections import OrderedDict

import tensorflow as tf

import component


class MultiCategoryClassifier(object):
    def __init__(self, cfg, architecture):
        self.cfg = cfg
        model_cfg = cfg.model

        Config = architecture.Config
        self.pretrain_cfg = Config.from_omegaconf(model_cfg.pretrain)

        self._error_check()

        if model_cfg.num_drop_hidden_layers > 0:
            self.pretrain_cfg.num_hidden_layers -= model_cfg.num_drop_hidden_layers

    def _error_check(self):
        model_cfg = self.cfg.model
        assert model_cfg.max_seq_length <= model_cfg.pretrain.max_position_embeddings

    def build_estimator(self, phase_cfg, train_steps=None):
        cfg = self.cfg
        model_cfg = cfg.model
        pretrain_cfg = self.pretrain_cfg

        # configurations
        gpu_options = tf.GPUOptions(allow_growth=True)
        sess_config = tf.ConfigProto(gpu_options=gpu_options, allow_soft_placement=True)
        distribution = tf.contrib.distribute.MirroredStrategy(num_gpus=cfg.num_gpu, auto_shard_dataset=True)

        save_checkpoints_steps = math.ceil(train_steps / 1) if train_steps else cfg.train.save_checkpoints_steps
    
        run_config = tf.contrib.tpu.RunConfig(
            master=None,
            save_summary_steps      = cfg.train.save_summary_steps,
            log_step_count_steps    = cfg.train.log_step_count_steps,
            model_dir               = cfg.path.model_dir,
            save_checkpoints_steps  = save_checkpoints_steps,
            train_distribute        = distribution,
            session_config          = sess_config)

        label2idx_dict_path = Path(cfg.path.model_dir, cfg.const.label2idx)
        label2idx_dict = json.loads(label2idx_dict_path.read_text(), object_pairs_hook=OrderedDict)
        input_floats_stat_path = Path(cfg.path.model_dir, cfg.const.input_floats_stat)
        input_floats_stat = json.loads(input_floats_stat_path.read_text(), object_pairs_hook=OrderedDict)
        num_labels_per_category = [len(label2idx_dict[key]) for key in label2idx_dict.keys()]
        warmup_steps = int(train_steps * cfg.train.warmup) if train_steps else None

        # Build model_fn
        # train-specific params don't affect other phase
        model_fn = component.model_fn_builder(
            bert_config     = pretrain_cfg,
            num_labels_per_category = num_labels_per_category,
            init_checkpoint = cfg.path.init_checkpoint,
            learning_rate   = cfg.train.learning_rate,
            num_train_steps = train_steps,
            num_warmup_steps= warmup_steps,
            eval_batch      = phase_cfg.batch_size,
            vocab_size      = pretrain_cfg.vocab_size,
            input_floats_stat = input_floats_stat)

        # Build estimator
        estimator = tf.estimator.Estimator(
            config  = run_config,
            model_fn= model_fn,
            params  = dict(batch_size = phase_cfg.batch_size, 
                      eval_batch_size = phase_cfg.eval_batch_size))

        return estimator

    def build_predictor(self, phase_cfg, infer_fn):
        estimator = self.build_estimator(phase_cfg)
        predictor = tf.contrib.predictor.from_estimator(estimator, infer_fn)

        return predictor
