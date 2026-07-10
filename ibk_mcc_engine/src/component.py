##
# MindsLab MCC component. 
# this code is mainly a copy of google-research/bert/run_classifier.py 
# and has some modifications
##

# coding=utf-8
# Copyright 2018 The Google AI Language Team Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""BERT finetuning runner."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
import re
import sys
import csv
import json
import collections
import numpy as np
import tensorflow as tf

from trfms.bert_google_tf1 import modeling
from trfms.bert_google_tf1 import optimization
from trfms.bert_google_tf1 import tokenization

class InputExample(object):
  """A single training/test example for simple sequence classification."""

  def __init__(self, guid, text_a, text_b=None, labels=None, input_floats=None):
    """Constructs a InputExample.
    Args:
      guid: Unique id for the example.
      text_a: string. The untokenized text of the first sequence. For single
        sequence tasks, only this sequence must be specified.
      text_b: (Optional) string. The untokenized text of the second sequence.
        Only must be specified for sequence pair tasks.
      labels: (Optional) list of string. The labels of the example. This should be
        specified for train and dev examples, but not for test examples.
    """
    self.guid = guid
    self.text_a = text_a
    self.text_b = text_b
    self.labels = labels
    self.input_floats = input_floats


class PaddingInputExample(object):
  """Fake example so the num input examples is a multiple of the batch size.
  When running eval/predict on the TPU, we need to pad the number of examples
  to be a multiple of the batch size, because the TPU requires a fixed batch
  size. The alternative is to drop the last batch, which is bad because it means
  the entire output data won't be generated.
  We use this class instead of `None` because treating `None` as padding
  battches could cause silent errors.
  """


class InputFeatures(object):
  """A single set of features of data."""

  def __init__(self,
               input_ids,
               input_mask,
               segment_ids,
               label_ids,
               input_floats,
               is_real_example=True):
    self.input_ids = input_ids
    self.input_mask = input_mask
    self.segment_ids = segment_ids
    self.label_ids = label_ids
    self.input_floats = input_floats
    self.is_real_example = is_real_example


class DataProcessor(object):
  """Base class for data converters for sequence classification data sets."""

  def get_train_examples(self, data_dir, labels):
    """Gets a collection of `InputExample`s for the train set."""
    raise NotImplementedError()

  def get_dev_examples(self, data_dir, labels):
    """Gets a collection of `InputExample`s for the dev set."""
    raise NotImplementedError()

  def get_test_examples(self, data_dir, labels):
    """Gets a collection of `InputExample`s for prediction."""
    raise NotImplementedError()

  def get_labels(self, data, data_dir):
    """Gets the list of labels for this data set."""
    raise NotImplementedError()

  @classmethod
  def _read_tsv(cls, input_file, quotechar=None):
    """Reads a tab separated value file."""
    try:
        csv.field_size_limit(sys.maxsize)
        with tf.gfile.Open(input_file, "r") as f:
          reader = csv.reader(f, delimiter="\t", quotechar=quotechar)
          lines = []
          for line in reader:
            lines.append(line)
    except:
        try:
            data = open(input_file).read().split('\n')[:-1]
        except:
            data = open(input_file, encoding='cp949').read().split('\n')[:-1]
        lines = []
        for d in data:
            lines.append(d.split('\t'))
    return lines


def convert_single_example(ex_index, example, max_seq_length, tokenizer, strint_dict, input_floats_stat):
  """Converts a single `InputExample` into a single `InputFeatures`."""

  if isinstance(example, PaddingInputExample):
      return InputFeatures(
          input_ids=[0] * max_seq_length,
          input_mask=[0] * max_seq_length,
          segment_ids=[0] * max_seq_length,
          label_ids=[0] * len(strint_dict),
          input_floats=[0.0] * input_floats_stat["num_input_floats"],
          is_real_example=False)

  tokens_a = tokenizer.tokenize(example.text_a)

  tokens_b = None
  if example.text_b:
    tokens_b = tokenizer.tokenize(example.text_b)

  if tokens_b:
    # Modifies `tokens_a` and `tokens_b` in place so that the total
    # length is less than the specified length.
    # Account for [CLS], [SEP], [SEP] with "- 3"
    _truncate_seq_pair(tokens_a, tokens_b, max_seq_length - 3)
  else:
    # Account for [CLS] and [SEP] with "- 2"
    if len(tokens_a) > max_seq_length - 2:
      tokens_a = tokens_a[0:(max_seq_length - 2)]

  # The convention in BERT is:
  # (a) For sequence pairs:
  #  tokens:   [CLS] is this jack ##son ##ville ? [SEP] no it is not . [SEP]
  #  type_ids: 0     0  0    0    0     0       0 0     1  1  1  1   1 1
  # (b) For single sequences:
  #  tokens:   [CLS] the dog is hairy . [SEP]
  #  type_ids: 0     0   0   0  0     0 0
  #
  # Where "type_ids" are used to indicate whether this is the first
  # sequence or the second sequence. The embedding vectors for `type=0` and
  # `type=1` were learned during pre-training and are added to the wordpiece
  # embedding vector (and position vector). This is not *strictly* necessary
  # since the [SEP] token unambiguously separates the sequences, but it makes
  # it easier for the models to learn the concept of sequences.
  #
  # For classification tasks, the first vector (corresponding to [CLS]) is
  # used as the "sentence vector". Note that this only makes sense because
  # the entire models is fine-tuned.
  tokens = []
  segment_ids = []
  tokens.append("[CLS]")
  segment_ids.append(0)
  for token in tokens_a:
    tokens.append(token)
    segment_ids.append(0)
  tokens.append("[SEP]")
  segment_ids.append(0)

  if tokens_b:
    for token in tokens_b:
      tokens.append(token)
      segment_ids.append(1)
    tokens.append("[SEP]")
    segment_ids.append(1)

  input_ids = tokenizer.convert_tokens_to_ids(tokens)

  # The mask has 1 for real tokens and 0 for padding tokens. Only real
  # tokens are attended to.
  input_mask = [1] * len(input_ids)

  # Zero-pad up to the sequence length.
  while len(input_ids) < max_seq_length:
    input_ids.append(0)
    input_mask.append(0)
    segment_ids.append(0)

  assert len(input_ids) == max_seq_length
  assert len(input_mask) == max_seq_length
  assert len(segment_ids) == max_seq_length

  # label_id = label_map[example.label]
  label_ids = example.labels

  input_floats = []
  for idx, input_float in enumerate(example.input_floats):
      input_float = float(input_float)
      input_float_normalized = (input_float - input_floats_stat["mean"][idx]) / input_floats_stat["std"][idx]
      input_floats.append(input_float_normalized)

  if 10 < ex_index < 15:
    tf.logging.info("*** Example ***")
    tf.logging.info("guid: %s" % (example.guid))
    tf.logging.info("tokens: %s" % " ".join(
        [tokenization.printable_text(x) for x in tokens]))
    tf.logging.info("input_ids: %s" % " ".join([str(x) for x in input_ids]))
    tf.logging.info("input_mask: %s" % " ".join([str(x) for x in input_mask]))
    tf.logging.info("segment_ids: %s" % " ".join([str(x) for x in segment_ids]))
    tf.logging.info("label_ids: %s" % " ".join([str(x) for x in label_ids]))
    tf.logging.info("input_floats: %s" % " ".join([str(x) for x in input_floats]))
    items = list(strint_dict.items())
    for idx, label in enumerate(example.labels):
        category, label2idx = items[idx]
        idx2label = {v:k for k, v in label2idx.items()}
        idx2label[-1] = 'NONE'
        tf.logging.info("category: %s | label: %s (id = %d)" % (category, idx2label[int(label)], label))

  feature = InputFeatures(
      input_ids=input_ids,
      input_mask=input_mask,
      segment_ids=segment_ids,
      label_ids=label_ids,
      input_floats=input_floats,
      is_real_example=True)
  return feature


def file_based_convert_examples_to_features(examples, tokenizer, output_file, max_seq_length, strint_dict, input_floats_stat):
  """Convert a set of `InputExample`s to a TFRecord file."""

  writer = tf.python_io.TFRecordWriter(output_file)

  for (ex_index, example) in enumerate(examples):
    if ex_index % 10000 == 1:
      tf.logging.info("Writing example %d of %d" % (ex_index, len(examples)))

    feature = convert_single_example(ex_index, example, max_seq_length, tokenizer, strint_dict, input_floats_stat)

    def create_int_feature(values):
      f = tf.train.Feature(int64_list=tf.train.Int64List(value=list(values)))
      return f

    def create_float_feature(values):
      f = tf.train.Feature(float_list=tf.train.FloatList(value=list(values)))
      return f

    features = collections.OrderedDict()
    features["input_ids"] = create_int_feature(feature.input_ids)
    features["input_mask"] = create_int_feature(feature.input_mask)
    features["segment_ids"] = create_int_feature(feature.segment_ids)
    features["label_ids"] = create_int_feature(feature.label_ids)
    features["is_real_example"] = create_int_feature([int(feature.is_real_example)])
    features["input_floats"] = create_float_feature(feature.input_floats)

    tf_example = tf.train.Example(features=tf.train.Features(feature=features))
    writer.write(tf_example.SerializeToString())
  writer.close()


def file_based_input_fn_builder(input_file, seq_length, num_category, num_input_floats, is_training, drop_remainder):
  """Creates an `input_fn` closure to be passed to TPUEstimator."""
  name_to_features = {
      "input_ids": tf.FixedLenFeature([seq_length], tf.int64),
      "input_mask": tf.FixedLenFeature([seq_length], tf.int64),
      "segment_ids": tf.FixedLenFeature([seq_length], tf.int64),
      "label_ids": tf.FixedLenFeature([num_category], tf.int64),
      "input_floats": tf.FixedLenFeature([num_input_floats], tf.float32),
      "is_real_example": tf.FixedLenFeature([], tf.int64),
  }

  def _decode_record(record, name_to_features):
    """Decodes a record to a TensorFlow example."""
    example = tf.parse_single_example(record, name_to_features)

    # tf.Example only supports tf.int64, but the TPU only supports tf.int32.
    # So cast all int64 to int32.
    for name in list(example.keys()):
      t = example[name]
      if t.dtype == tf.int64:
        t = tf.to_int32(t)
      example[name] = t

    return example

  def input_fn(params):
    """The actual input function."""
    if is_training:
        batch_size = params["batch_size"]
    else:
        batch_size = params["eval_batch_size"]

    # For training, we want a lot of parallel reading and shuffling.
    # For eval, we want no shuffling and parallel reading doesn't matter.
    d = tf.data.TFRecordDataset(input_file)
    if is_training:
      d = d.repeat()
      d = d.shuffle(buffer_size=100)

    d = d.apply(
        tf.contrib.data.map_and_batch(
            lambda record: _decode_record(record, name_to_features),
            batch_size=batch_size,
            drop_remainder=drop_remainder))

    return d

  return input_fn


def _truncate_seq_pair(tokens_a, tokens_b, max_length):
  """Truncates a sequence pair in place to the maximum length."""

  # This is a simple heuristic which will always truncate the longer sequence
  # one token at a time. This makes more sense than truncating an equal percent
  # of tokens from each, since if one sequence is very short then each token
  # that's truncated likely contains more information than a longer sequence.
  while True:
    total_length = len(tokens_a) + len(tokens_b)
    if total_length <= max_length:
      break
    if len(tokens_a) > len(tokens_b):
      tokens_a.pop()
    else:
      tokens_b.pop()


def model_fn_builder(bert_config, num_labels_per_category, init_checkpoint, learning_rate, num_train_steps, 
            num_warmup_steps, eval_batch, input_floats_stat=None, use_tpu=False, use_one_hot_embeddings=False, vocab_size=None):
  """Returns `model_fn` closure for TPUEstimator."""

  def model_fn(features, labels, mode, params):  # pylint: disable=unused-argument
    """The `model_fn` for TPUEstimator."""

    tf.logging.info("*** Features ***")
    for name in sorted(features.keys()):
      tf.logging.info("  name = %s, shape = %s" % (name, features[name].shape))

    input_ids = features["input_ids"]
    input_mask = features["input_mask"]
    segment_ids = features["segment_ids"]
    label_ids = features["label_ids"]
    input_floats = features["input_floats"]

    if "is_real_example" in features:
      is_real_example = tf.cast(features["is_real_example"], dtype=tf.float32)
    else:
      is_real_example = tf.ones(tf.shape(label_ids), dtype=tf.float32)

    is_training = (mode == tf.estimator.ModeKeys.TRAIN)
    (logits_by_category, probabilities_by_category) = create_model(
        bert_config, is_training, input_ids, input_mask, segment_ids, num_labels_per_category, input_floats, 
        input_floats_stat["num_input_floats"], use_one_hot_embeddings, vocab_size)

    tvars = tf.trainable_variables()
    initialized_variable_names = {}
    scaffold_fn = None
    if init_checkpoint:
      (assignment_map, initialized_variable_names
      ) = modeling.get_assignment_map_from_checkpoint(tvars, init_checkpoint)
      if use_tpu:

        def tpu_scaffold():
          tf.train.init_from_checkpoint(init_checkpoint, assignment_map)
          return tf.train.Scaffold()

        scaffold_fn = tpu_scaffold
      else:
        tf.train.init_from_checkpoint(init_checkpoint, assignment_map)

    output_spec = None
    if mode == tf.estimator.ModeKeys.TRAIN:
      log_probs = tf.concat([tf.nn.log_softmax(logits, axis=-1) for logits in logits_by_category], axis=-1)

      hot_labels = []
      for label_id, num_label in zip(tf.unstack(label_ids, axis=-1), num_labels_per_category):
          if label_id != -1:
              hot_label = tf.one_hot(label_id, depth=num_label, dtype=tf.float32)
          else:
              hot_label = tf.zeros(shape=[num_label])
          hot_labels.append(hot_label)

      one_hot_labels = tf.concat(hot_labels, axis=-1)
      per_example_loss = -tf.reduce_sum(one_hot_labels * log_probs, axis=-1)
      total_loss = tf.reduce_mean(per_example_loss)

      train_op = optimization.create_optimizer(
          total_loss, learning_rate, num_train_steps, num_warmup_steps, tvars)

      output_spec = tf.estimator.EstimatorSpec(
          mode=mode,
          loss=total_loss,
          train_op=train_op,
          scaffold=scaffold_fn)
    elif mode == tf.estimator.ModeKeys.EVAL:
      log_probs = tf.concat([tf.nn.log_softmax(logits, axis=-1) for logits in logits_by_category], axis=-1)
      one_hot_labels = tf.concat([tf.one_hot(label_id, depth=num_labels, dtype=tf.float32) for label_id in label_ids], axis=-1)
      per_example_loss = -tf.reduce_sum(one_hot_labels * log_probs, axis=-1)
      total_loss = tf.reduce_mean(per_example_loss)

      def metric_fn(per_example_loss, label_ids, logits, is_real_example):
        predictions = [tf.argmax(_logits, axis=-1, output_type=tf.int32) for _logits in logits]
        accuracy = tf.metrics.accuracy(
            labels=label_ids, predictions=predictions, weights=is_real_example)
        loss = tf.metrics.mean(values=per_example_loss, weights=is_real_example)

        result_dict = {
            "eval_accuracy": accuracy,
            "eval_loss": loss,
        }
        return result_dict

      eval_metrics = (metric_fn,
                      [per_example_loss, label_ids, logits_by_category, is_real_example])
      output_spec = tf.estimator.EstimatorSpec(
          mode=mode,
          loss=total_loss,
          eval_metric_ops=eval_metrics,
          scaffold=scaffold_fn)
    else:
      output_spec = tf.estimator.EstimatorSpec(
          mode=mode,
          predictions={"probabilities": probabilities_by_category, "labels": label_ids},
          scaffold=scaffold_fn)
    return output_spec

  return model_fn


def create_model(bert_config, is_training, input_ids, input_mask, segment_ids, num_labels_per_category, input_floats,
        num_input_floats=0, use_one_hot_embeddings=False, vocab_size=None, eval_batch=None):
  """Creates a classification models."""
  model = modeling.BertModel(
      config=bert_config,
      is_training=is_training,
      input_ids=input_ids,
      input_mask=input_mask,
      token_type_ids=segment_ids,
      use_one_hot_embeddings=use_one_hot_embeddings,
      vocab_size=vocab_size)

  output_layer = model.get_pooled_output()
  hidden_size = output_layer.shape[-1].value

  head_size = hidden_size // bert_config.num_attention_heads

  if num_input_floats:
      float_input_weights = tf.get_variable(
          "float_input_weights", [head_size, num_input_floats],
          initializer=tf.truncated_normal_initializer(stddev=0.02))
      float_input_bias = tf.get_variable(
          "float_input_bias", [head_size], initializer=tf.zeros_initializer())
      float_features = tf.nn.bias_add(tf.matmul(input_floats, float_input_weights, transpose_b=True), float_input_bias)
      output_layer = tf.concat([output_layer, float_features], axis=-1)

  flattened_num_labels = sum(num_labels_per_category)

  output_params_per_category = []
  for idx, num_labels in enumerate(num_labels_per_category):
      output_weights = tf.get_variable(
          "output_weights_{}".format(idx), [num_labels, hidden_size + head_size * bool(num_input_floats)],
          initializer=tf.truncated_normal_initializer(stddev=0.02))
      output_bias = tf.get_variable(
          "output_bias_{}".format(idx), [num_labels], initializer=tf.zeros_initializer())

      output_params_per_category.append((output_weights, output_bias))

  intra_class_weights = tf.get_variable(
      "intra_class_weights", [flattened_num_labels, flattened_num_labels],
      initializer=tf.truncated_normal_initializer(stddev=0.02))

  intra_class_bias = tf.get_variable(
      "intra_class_bias", [flattened_num_labels],
      initializer=tf.truncated_normal_initializer(stddev=0.02))

  with tf.variable_scope("loss"):
    if is_training:
      # I.e., 0.1 dropout
      output_layer = tf.nn.dropout(output_layer, keep_prob=0.9)

    flows = [tf.nn.bias_add(tf.matmul(output_layer, output_weights, transpose_b=True), output_bias)
                for output_weights, output_bias in output_params_per_category]
    flow = tf.concat(flows, axis=-1)
    logits = tf.nn.bias_add(tf.matmul(flow, intra_class_weights, transpose_b=True), intra_class_bias)
    logits += flow

    logits_by_category = tf.split(logits, num_labels_per_category, axis=-1)

    probabilities_by_category = tf.concat([tf.nn.softmax(logits, axis=-1) for logits in logits_by_category], axis=-1)

    return (logits_by_category, probabilities_by_category)


# This function is not used by this file but is still used by the Colab and
# people who depend on it.
def input_fn_builder(features, seq_length, is_training, drop_remainder, num_category=1):
  """Creates an `input_fn` closure to be passed to TPUEstimator."""

  all_input_ids = []
  all_input_mask = []
  all_segment_ids = []
  all_label_ids = []

  for feature in features:
    all_input_ids.append(feature.input_ids)
    all_input_mask.append(feature.input_mask)
    all_segment_ids.append(feature.segment_ids)
    all_label_ids.append(feature.label_ids)

  def input_fn(params):
    """The actual input function."""
    batch_size = params["batch_size"]

    num_examples = len(features)

    # This is for demo purposes and does NOT scale to large data sets. We do
    # not use Dataset.from_generator() because that uses tf.py_func which is
    # not TPU compatible. The right way to load data is with TFRecordReader.
    d = tf.data.Dataset.from_tensor_slices({
        "input_ids":
            tf.constant(
                all_input_ids, shape=[num_examples, seq_length],
                dtype=tf.int32),
        "input_mask":
            tf.constant(
                all_input_mask,
                shape=[num_examples, seq_length],
                dtype=tf.int32),
        "segment_ids":
            tf.constant(
                all_segment_ids,
                shape=[num_examples, seq_length],
                dtype=tf.int32),
        "label_ids":
            tf.constant(all_label_ids, shape=[num_examples, num_category], dtype=tf.int32),
    })

    if is_training:
      d = d.repeat()
      d = d.shuffle(buffer_size=100)

    d = d.batch(batch_size=batch_size, drop_remainder=drop_remainder)
    return d

  return input_fn


# Used by infer_fn
def convert_examples_to_features(examples, max_seq_length, tokenizer, strint_dict, input_floats_stat):
  """Convert a set of `InputExample`s to a list of `InputFeatures`."""

  features = []
  for (ex_index, example) in enumerate(examples):
    if ex_index % 10000 == 1:
      tf.logging.info("Writing example %d of %d" % (ex_index, len(examples)))

    feature = convert_single_example(ex_index, example, max_seq_length, tokenizer, strint_dict, input_floats_stat)

    features.append(feature)
  return features


def batchnorm_layer(inputT, is_training, name=None):
    # Note: is_training is tf.placeholder(tf.bool) type
    return tf.cond(is_training,
                   lambda: tf.layers.batch_normalization(
                       inputT, training=is_training,
                       center=True, scale=True),

                   lambda: tf.layers.batch_normalization(
                       inputT, training=is_training,
                       center=True, scale=True, reuse=True))


def conv_1d_with_batch(inputs, num_featrue, kernel, is_training, trainable, name):
   with tf.variable_scope(name + '_conv1d'):
       conv_layer = tf.layers.conv1d(inputs,
                                     filters=num_featrue,
                                     kernel_size=kernel,
                                     strides=1,
                                     padding='SAME',
                                     activation=None,
                                     kernel_initializer=tf.truncated_normal_initializer(stddev=0.02),
                                     use_bias=False,
                                     name=name,
                                     trainable=trainable)
       # conv_batch_norm = batchnorm_layer(conv_layer, is_training, name + '_batchnorm')
   return tf.nn.relu(conv_layer)


def task_specific_attention(inputs, output_size, projection_output,
                            initializer=tf.truncated_normal_initializer(stddev=0.02),
                            activation_fn=tf.tanh, temp=1, scope=None):
    """
    Performs task-specific attention reduction, using learned
    attention context vector (constant within task of interest).
    Args:
        inputs: Tensor of shape [batch_size, units, input_size]
            `input_size` must be static (known)
            `units` axis will be attended over (reduced from output)
            `batch_size` will be preserved
        output_size: Size of output's inner (feature) dimension
    Returns:
        output: Tensor of shape [batch_size, output_dim].
    """
    assert len(inputs.get_shape()) == 3 and inputs.get_shape()[-1].value is not None

    with tf.variable_scope(scope or 'attention') as scope:
        attention_context_vector = tf.get_variable(name='attention_context_vector',
                                                   shape=[output_size],
                                                   initializer=initializer,
                                                   dtype=tf.float32)

        input_projection = tf.contrib.layers.fully_connected(inputs, output_size,
                                                             activation_fn=activation_fn,
                                                             scope=scope)

        vector_attn = tf.reduce_sum(tf.multiply(input_projection, attention_context_vector), axis=2, keepdims=True)

        attention_weights = tf.nn.softmax(vector_attn/temp, axis=1)
        weighted_projection = tf.multiply(input_projection, attention_weights)

        if projection_output == False:
            outputs = tf.reduce_sum(weighted_projection, axis=1)
            return outputs, attention_weights
        else:
            return weighted_projection, attention_weights


def global_pooling(inputs, scope_name, pooling_type='concat'):
   with tf.variable_scope(scope_name + '_Global_average_pooling'):
       gap = tf.layers.average_pooling1d(inputs,
                                         pool_size=int(inputs.shape[1]),
                                         strides=1, padding='valid')
       gap = tf.reduce_mean(gap, axis=1)

   with tf.variable_scope(scope_name + '_Global_max_pooling'):
       gmp = tf.layers.max_pooling1d(inputs,
                                     pool_size=int(inputs.shape[1]),
                                     strides=1, padding='valid')
       gmp = tf.reduce_mean(gmp, axis=1)

   with tf.variable_scope(scope_name + '_pooling_concat_layer'):
       pooling_concat_layer = tf.concat([gap, gmp], axis=-1)

   if pooling_type == 'average':
       return gap
   elif pooling_type == 'max':
       return gmp
   elif pooling_type == 'concat':
       return pooling_concat_layer
