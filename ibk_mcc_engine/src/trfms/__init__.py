# This file functions as an indexer for various transformers architecture
# List
# - BERT: 
# - ALBERT:
from types import SimpleNamespace
from trfms.bert_google_tf1.modeling import BertModel, BertConfig
from trfms.albert_google_tf1.modeling import AlBertModel, AlBertConfig

def find_architecture(name):
    if name == 'bert':
        return SimpleNamespace(Model=BertModel, Config=BertConfig)
    elif name == 'albert':
        return SimpleNamespace(Model=AlBertModel, Config=AlBertConfig)
