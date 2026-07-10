# MindsLab Inc.
# Multi-Category Classifier Data Processor

import tensorflow as tf

import time, random, re, json, collections
import numpy as np
from sklearn.metrics import classification_report, confusion_matrix
from pathlib import Path
from tqdm import tqdm
from collections import OrderedDict

import component
from component import DataProcessor, InputExample
from trfms.bert_google_tf1 import tokenization
from trfms import utils

class MccDataProcessor():
    def __init__(self, cfg):
        self.cfg = cfg

        self.model_dir_path = Path(cfg.path.model_dir)

        self.tokenizer = tokenization.FullTokenizer(vocab_file=cfg.path.vocab_file, do_lower_case=cfg.model.do_lower_case)
        self._processor = MccRawDataProcessor(cfg)
        _, self.label2idx_dict_by_category = self._processor.get_labels()
        self.idx2label_by_category = OrderedDict()

        for category, label2idx_dict in self.label2idx_dict_by_category.items():
            self.idx2label_by_category[category] = { idx:label for label, idx in label2idx_dict.items() }

        self.input_floats_stat = self._processor.get_input_floats_stat()

    def build_train_input_fn(self, train_cfg):
        cfg = self.cfg
        model_cfg = cfg.model
        is_training = True      # Now unnecessary, but for future generalization into 'build_input_fn'

        # Load data file into a list of InputExamples
        examples = self._processor.get_file_examples(train_cfg.data, phase='train')

        if is_training:
            seed = int(time.time()) % cfg.seed_bucket
            print('Seed: ', seed)
            random.Random(seed).shuffle(examples) #TODO temporary commenting out

        # Tokenize and write data into a serialized tfrecord file to improve process time
        data_path = Path(train_cfg.data)
        tfrecord_path = data_path.parent.joinpath(model_cfg.tokenizer, data_path.stem).with_suffix('.tfrecord')
        if not tfrecord_path.exists():
            tfrecord_path.parent.mkdir(parents=True, exist_ok=True)

            component.file_based_convert_examples_to_features(
                    examples        = examples,
                    tokenizer       = self.tokenizer,
                    output_file     = str(tfrecord_path),
                    max_seq_length  = model_cfg.max_seq_length,
                    strint_dict     = self.label2idx_dict_by_category,
                    input_floats_stat= self.input_floats_stat)

        # Build input_fn using tfrecord
        input_fn = component.file_based_input_fn_builder(
            input_file      = str(tfrecord_path),
            seq_length      = model_cfg.max_seq_length,
            num_category    = len(self.label2idx_dict_by_category),
            num_input_floats = self.input_floats_stat["num_input_floats"],
            is_training     = is_training,
            drop_remainder  = is_training)

        return input_fn, examples

    # TODO maybe there is a better way
    def build_infer_fn(self):
        model_cfg = self.cfg.model
        num_category = len(self.label2idx_dict_by_category)

        features = {
            'input_ids':   tf.placeholder(tf.int32, shape=[None, model_cfg.max_seq_length], name='input_ids'),
            'input_mask':  tf.placeholder(tf.int32, shape=[None, model_cfg.max_seq_length], name='input_mask'),
            'segment_ids': tf.placeholder(tf.int32, shape=[None, model_cfg.max_seq_length], name='segment_ids'),
            'label_ids':   tf.placeholder(tf.int32, shape=[None, num_category], name='label_ids'),
            'input_floats':   tf.placeholder(tf.float32, shape=[None, self.input_floats_stat["num_input_floats"]], name='input_floats'),
        }
        infer_fn = tf.estimator.export.build_raw_serving_input_receiver_fn(features)

        return infer_fn

    def build_test_examples(self, test_data_file):
        examples = self._processor.get_file_examples(test_data_file, phase='test')
        return examples

    def build_infer_examples(self, infer_texts, input_floats=None):
        examples = self._processor.get_infer_examples(infer_texts, input_floats)
        return examples

    def build_infer_features(self, examples):
        model_cfg = self.cfg.model

        _features = component.convert_examples_to_features(examples, model_cfg.max_seq_length, self.tokenizer, self.label2idx_dict_by_category, self.input_floats_stat)

        features = {
            'input_ids': [],
            'input_mask': [],
            'segment_ids': [],
            'label_ids': [],
            'input_floats': [],
        }
        for feature in _features:
            features['input_ids'].append(feature.input_ids)
            features['input_mask'].append(feature.input_mask)
            features['segment_ids'].append(feature.segment_ids)
            features['label_ids'].append(feature.label_ids)
            features['input_floats'].append(feature.input_floats)

        return features

    def postprocess_batches(self, batched_inputs, batched_outputs):
        cfg = self.cfg

        # extend batched results into concatenated records
        inputs = []
        predictions = []
        for batched_input, batched_output in zip(batched_inputs, batched_outputs):
            probs      = batched_output['probabilities']
            label_ids  = batched_output['labels']

            # the order of tuple elements is important. it's for efficiency to choose the tuple structure.
            # it's temporary way of branching between infer(ndim==2), test(ndim==1)
            if probs.ndim == 2:
                predictions.extend(zip(probs))
                inputs.extend(batched_input)
            elif probs.ndim == 1:
                predictions.append((probs))
                inputs.append(batched_input)

        results = []
        for _input, prediction in zip(inputs, predictions):
            origin_context = _input.text_a
            origin_label_ids = _input.labels
            origin_input_floats = _input.input_floats

            (probs,) = prediction

            num_labels = [len(labels) for labels in self.idx2label_by_category.values()]
            borders = [sum(num_labels[:i]) for i in range(1, len(num_labels))]

            probs_by_category = np.split(probs, borders, axis=-1)
            top_pred_idxes_by_category  = [probs.argsort()[::-1][:cfg.infer.num_top_label] for probs in probs_by_category]
            top_pred_probs_by_category  = [np.take(probs, top_pred_idxes)
                                                for probs, top_pred_idxes in zip(probs_by_category, top_pred_idxes_by_category)]
            top_predictions_by_category = [list(zip(top_pred_idxes, top_pred_probs))
                                                for top_pred_idxes, top_pred_probs in zip(top_pred_idxes_by_category, top_pred_probs_by_category)]

            results.append((origin_context, origin_input_floats, origin_label_ids, top_predictions_by_category))
        return results

    def evaluate_result(self, results, info=''):
        cfg = self.cfg
        prediction_path = self.model_dir_path.joinpath(cfg.const.prediction)

        categories = self.label2idx_dict_by_category.keys()
        labels_by_category = [[] for _ in range(len(categories))]
        preds_by_category = [[] for _ in range(len(categories))]
        scores_top1 = [0] * len(categories)
        scores_top2 = [0] * len(categories)
        scores_top3 = [0] * len(categories)
        cm_report_by_category = {}

        with prediction_path.open('w') as f:
            num_written_lines = 0
            for result_idx, result in tqdm(enumerate(results), desc='Writing result'):
                context,input_floats ,label_ids, top_preds_by_category = result
                f.write('[#{}]\n{}\n{}\n'.format(result_idx + 1, context,input_floats))

                for idx, (category, top_preds, label_idx) in enumerate(
                        zip(categories, top_preds_by_category, label_ids)):
                    top_pred_idx, top_pred_prob = top_preds[0]  # top-1
                    preds_by_category[idx].append(top_pred_idx)
                    labels_by_category[idx].append(label_idx)

                    # Check for top1 accuracy
                    if top_pred_idx == label_idx:
                        scores_top1[idx] += 1

                    # Check for top2 accuracy (only if not top1)
                    elif label_idx in [pred_idx for pred_idx, _ in top_preds[1:2]]:
                        scores_top2[idx] += 1

                    # Check for top3 accuracy (only if not top1 or top2)
                    elif label_idx in [pred_idx for pred_idx, _ in top_preds[2:3]]:
                        scores_top3[idx] += 1

                    # Write label information
                    if label_idx in self.idx2label_by_category[category]:
                        label_name = self.idx2label_by_category[category][label_idx]
                        f.write('<{}> Label: {}({})\n'.format(category, label_idx, label_name))
                    else:
                        label_name = "Unknown"
                        print(
                            f"Warning: label_idx {label_idx} not found in idx2label_by_category for category {category}")

                    for pred_idx, pred_prob in top_preds:
                        pred_name = self.idx2label_by_category[category].get(pred_idx, "Unknown")
                        f.write('- Pred: {}({}) {:.2f}%\n'.format(pred_idx, pred_name, pred_prob * 100))

                f.write('\n')
                num_written_lines += 1

            for category, labels, preds, score1, score2, score3 in zip(categories, labels_by_category,
                                                                       preds_by_category, scores_top1, scores_top2,
                                                                       scores_top3):
                label_string_pairs = list(sorted(self.label2idx_dict_by_category[category].items(), key=lambda x: x[1]))
                label_strings, label_idxes = list(zip(*label_string_pairs))

                cr = classification_report(labels, preds, labels=label_idxes, target_names=label_strings)
                cm = confusion_matrix(labels, preds, labels=label_idxes)
                cm_report_by_category[category] = classification_report(labels, preds, output_dict=True)

                acc_top1 = score1 / num_written_lines
                acc_top2 = score2 / num_written_lines
                acc_top3 = score3 / num_written_lines

                # Total Acc is sum of top1, top2, top3 accuracies
                total_acc = acc_top1 + acc_top2 + acc_top3

                f.write(
                    '===== {} =====\nTop1 Acc: {:.4f}\nTop2 Acc: {:.4f}\nTop3 Acc: {:.4f}\nTotal Acc: {:.4f}\n\nClassification Report\n {}\nConfusion Matrix\n {}\n\n'.format(
                        category, acc_top1, acc_top2, acc_top3, total_acc, cr, cm))
                print('===== {} ====='.format(category))
                print('Top1 Acc: {:.4f}'.format(acc_top1))
                print('Top2 Acc: {:.4f}'.format(acc_top2))
                print('Top3 Acc: {:.4f}'.format(acc_top3))
                print('Total Acc: {:.4f}'.format(total_acc))
                print('Classification Report:\n {}\n'.format(cr))
                print('Confusion Matrix:\n {}\n'.format(cm))

        assert num_written_lines == len(results)
        print(cm_report_by_category)
        print('====================')
        return cm_report_by_category


class MccRawDataProcessor(DataProcessor):
    """Processor for the ITC data set """
    def __init__(self, cfg):
        self.cfg = cfg
        self._prepare()

    def _prepare(self):
        cfg = self.cfg

        model_dir_path = Path(cfg.path.model_dir)
        label2idx_path = model_dir_path.joinpath(cfg.const.label2idx)
        print(cfg.const)
        input_floats_stat_path = model_dir_path.joinpath(cfg.const.input_floats_stat)

        if not label2idx_path.exists() or not input_floats_stat_path.exists():  # first training phase
            with Path(cfg.train.data).open('r') as f:
                lines = f.readlines()
            header, records = lines[0], lines[1:]
            categories = header.rstrip().split('\t')[1:]

            labels_by_category = OrderedDict()
            label2idx_dict_by_category = OrderedDict()
            input_floats_stat = OrderedDict()

            for category in categories:
                labels_by_category[category] = []

            input_floats_values = []
            for record in records:
                record_inputs_labels = record.strip(' \n\r').split('\t')
                record_inputs, record_labels = record_inputs_labels[0], record_inputs_labels[1:]
                #print(record_inputs,record_labels)
                for category, label in zip(categories, record_labels):
                    if label != '':
                        labels_by_category[category].append(label)
                input_floats_values.append(list(map(float, record_inputs.split(cfg.const.input_separator)[1:])))

            input_floats = np.array(input_floats_values)
            print(input_floats)
            input_floats_stat["num_input_floats"] = input_floats.shape[1]
            input_floats_stat["max"] = list(input_floats.max(axis=0))
            input_floats_stat["min"] = list(input_floats.min(axis=0))
            input_floats_stat["mean"] = list(input_floats.mean(axis=0))
            input_floats_stat["std"] = list(input_floats.std(axis=0))

            for category, _labels in labels_by_category.items():
                labels = sorted(set(_labels))
                labels_by_category[category] = labels
                label2idx_dict_by_category[category] = { label:idx for (idx, label) in enumerate(labels) }

            with label2idx_path.open('w') as f:
                json.dump(label2idx_dict_by_category, f, ensure_ascii=False, indent=4)
            with input_floats_stat_path.open('w') as f:
                json.dump(input_floats_stat, f, ensure_ascii=False, indent=4)
        else:
            with label2idx_path.open('r') as f:
                label2idx_dict_by_category = json.load(f, object_pairs_hook=OrderedDict)
            labels_by_category = OrderedDict()
            for category, label2idx_dict in label2idx_dict_by_category.items():
                labels_by_category[category] = list(label2idx_dict.keys())
            with input_floats_stat_path.open('r') as f:
                input_floats_stat = json.load(f, object_pairs_hook=OrderedDict)

        self.labels_by_category = labels_by_category
        self.label2idx_dict_by_category = label2idx_dict_by_category
        self.input_floats_stat = input_floats_stat

    def get_file_examples(self, data_file, phase=None):
        return self._create_examples(
            self._read_tsv(data_file), phase)

    def get_infer_examples(self, infer_texts, input_floats=None):

        input_header = self.cfg.const.input_separator.join(['TEXT'] + list(map(str, range(self.input_floats_stat["num_input_floats"]))))
        dummy_header = [input_header] + list(self.label2idx_dict_by_category.keys())
        dummy_labels = [None] * len(self.label2idx_dict_by_category)

        input_floats = input_floats or []
        if len(input_floats) == 0 or (len(input_floats) > 0 and type(input_floats[0]) is not list):
            input_floats = [input_floats] * len(infer_texts)

        infer_lines = [dummy_header]
        for text, floats in zip(infer_texts, input_floats):
            input_str = self.cfg.const.input_separator.join([text] + list(map(str, floats)))
            infer_lines.append((input_str,) + tuple(dummy_labels))

        return self._create_examples(infer_lines, 'infer')

    def get_labels(self):
        return self.labels_by_category, self.label2idx_dict_by_category

    def get_input_floats_stat(self):
        return self.input_floats_stat

    def _create_examples(self, lines, set_type=None):
        header, records = lines[0], lines[1:]
        num_floats = self.input_floats_stat["num_input_floats"]
        categories = header[1:]
        label2idx_dict_by_col_idx = [self.label2idx_dict_by_category[category] for category in categories]

        examples = []
        for (i, line) in enumerate(records):
            guid = "{}-{}".format(set_type, i)
            inputs = line[0].split(self.cfg.const.input_separator)
            text, input_floats = inputs[0], inputs[1:]
            text_a = tokenization.convert_to_unicode(text)
            labels = []

            for idx, (label, label2idx_dict) in enumerate(zip(line[1:], label2idx_dict_by_col_idx)):
                label_id = label2idx_dict[label] if label in label2idx_dict else -1  # -1 for None
                labels.append(label_id)
            example = InputExample(guid=guid, text_a=text_a, text_b=None, labels=labels, input_floats=input_floats)
            examples.append(example)
        return examples
