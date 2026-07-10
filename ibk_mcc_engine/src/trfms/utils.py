import re
import nltk
import unicodedata
import numpy as np

def split_into_sentences(text):
    try:
        detected = detect(text)
    except:
        detected = 'else'
  
    if detected == 'zh-cn':
        def zng(paragraph):
            for sent in re.findall(u'[^!?。\.\!\?]+[!?。\.\!\?]?', paragraph, flags=re.U):
                yield sent
        out = list(zng(text))
        sents = sum([i.split('\n') for i in out], [])
    elif detected == 'ru':
        out = nltk.sent_tokenize(text, language="russian")
        sents = sum([i.split('\n') for i in out], [])
    elif detected == 'en':
        out = nltk.sent_tokenize(text, language="english")
        sents = sum([i.split('\n') for i in out], [])
    elif detected == 'ja':
        jp_sent_tokenizer = nltk.RegexpTokenizer('[^ 「」!?。．）]*[!?。]')
        out = jp_sent_tokenizer.tokenize(text)
        sents = sum([i.split('\n') for i in out], [])
    else:
        ending_chars = r'[다나죠요까냐라자오네가마아세데지게군먼려든어래리]'
        ending_marks = r'[\.?!。]+'
        ending_exceptions = r'[\'">)}\]」』”’]'
        sep_token = '<<SEP>>'
        
        converted = re.sub(r'(?<={})(?P<ending_mark>{})(?!{})[\s]*'.format(ending_chars, ending_marks, ending_exceptions), 
                     r'\g<ending_mark>{}'.format(sep_token),
                     text)
        sents = converted.split(sep_token)

    return sents[:-1] if len(sents) > 1 and sents[-1] == '' else sents

def is_whitespace(c):
    if c == " " or c == "\t" or c == "\r" or c == "\n" or ord(c) == 0x202F:
        return True
    cat = unicodedata.category(c)
    if cat == "Zs":
        return True
    return False


def _is_control(char):
  """Checks whether `chars` is a control character."""
  # These are technically control characters but we count them as whitespace
  # characters.
  if char == "\t" or char == "\n" or char == "\r":
    return False
  cat = unicodedata.category(char)
  if cat.startswith("C"):
    return True
  return False


def clean_text(text):
    """Performs invalid character removal and whitespace cleanup on text."""
    output = []
    for char in text:
      cp = ord(char)
      if cp == 0 or cp == 0xfffd or _is_control(char):
        continue
      if is_whitespace(char):
        output.append(" ")
      else:
        output.append(char)
    return "".join(output)


def is_punctuation(char):
  """Checks whether `chars` is a punctuation character."""
  cp = ord(char)
  # We treat all non-letter/number ASCII as punctuation.
  # Characters such as "^", "$", and "`" are not in the Unicode
  # Punctuation class but we treat them as punctuation anyways, for
  # consistency.
  if ((cp >= 33 and cp <= 47) or (cp >= 58 and cp <= 64) or
      (cp >= 91 and cp <= 96) or (cp >= 123 and cp <= 126)):
    return True
  cat = unicodedata.category(char)
  if cat.startswith("P"):
    return True
  return False


# it's not good way to clean html tag. TODO modify this with using html parser such as bs4
def remove_tag(content):
   cleanr = re.compile('<.*?>')
   cleantext = re.sub(cleanr, '', content)
   return cleantext


# what is the difference between the below and x.replace(' ', '')
# -> it's removing spaces in a list
def rm_sp(x):
    while 1:
        try:
            x.remove('')
        except:
            try:
                x.remove(' ')
            except:
                break
    return x


def softmax(x):
    """Compute softmax values for each sets of scores in x."""
    e_x = np.exp(x - np.max(x))
    return e_x / e_x.sum(axis=0)


def tokenizer_encode(passage, tokenizer):
    doc_tokens = []
    char_to_word_offset = []
    prev_is_whitespace = True
    for c in passage:
        if is_whitespace(c):
            prev_is_whitespace = True
            doc_tokens.append(' ')
        else:
            if prev_is_whitespace:
                doc_tokens.append(c)
            else:
                doc_tokens[-1] += c
            prev_is_whitespace = False
        char_to_word_offset.append(len(doc_tokens) - 1)

    tok_to_orig_index = []
    orig_to_tok_index = []
    sub_tokens_length = []
    all_doc_tokens = []
    for (i, token) in enumerate(doc_tokens):
        orig_to_tok_index.append(len(all_doc_tokens))
        sub_tokens = tokenizer.tokenize(token)
        if '[UNK]' in sub_tokens:
            try:
                if detect(passage) != 'zh-cn':
                    sub_tokens = [token]
            except:
                sub_tokens = [token]
        if sub_tokens == []:
            try:
                sub_tokens_length[-1] += 1
            except:
                sub_tokens_length.append(1)
        else:
            for sub_token in sub_tokens:
                tok_to_orig_index.append(i)
                if sub_token == '[UNK]':
                    sub_tokens_length.append(1)
                else:
                    sub_tokens_length.append(len(sub_token.replace(' ##', '').replace('##', '')))
                all_doc_tokens.append(sub_token)

    return all_doc_tokens, sub_tokens_length


# what is this for? -> to cover [UNK] token such as hanja
def tokenizer_encode_keep_idxes(passage, tokenizer):
    words_and_spaces = []
    prev_is_whitespace = True
    for c in passage:
        if is_whitespace(c):
            words_and_spaces.append(' ')
            prev_is_whitespace = True
        else:
            if prev_is_whitespace:
                words_and_spaces.append(c)
            else:
                words_and_spaces[-1] += c
            prev_is_whitespace = False
    all_doc_tokens = []
    sub_tokens_length = []
    for (i, word_or_space) in enumerate(words_and_spaces):
        sub_tokens = tokenizer.tokenize(word_or_space)
        '''
        # it's merging tokens into one word if there is at least one unknown token.
        if '[UNK]' in sub_tokens:
            try:
                if detect(passage) != 'zh-cn':
                    sub_tokens = [word_or_space]
            except:
                sub_tokens = [word_or_space]
        '''
        if sub_tokens == []:
            try:
                sub_tokens_length[-1] += 1
            except:
                sub_tokens_length.append(1)
        else:
            for sub_token in sub_tokens:
                if sub_token == '[UNK]':
                    # print('>>>', word_or_space, sub_tokens)       # unknown whole word? not coming here
                    sub_tokens_length.append(1)
                else:
                    sub_tokens_length.append(len(sub_token.replace(' ##', '').replace('##', '')))
                all_doc_tokens.append(sub_token)
    return all_doc_tokens, sub_tokens_length
