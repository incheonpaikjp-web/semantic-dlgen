from __future__ import print_function

from keras.models import Model
from keras.layers import Input, LSTM, Dense
import numpy as np
from keras import backend as K

import sys
args = sys.argv

import pickle

batch_size = 64  # バッチサイズ
epochs = 100  # エポックサイズ.
latent_dim = 256  # エンコーディングの次元数
num_samples = 10000  # サンプル数
# データファイルのパス
#data_path = '/home/ataka/Documents/rep/python/Seq2seq/jpn.txt'
data_path = args[1]

K.clear_session()


input_texts = []  #英語のデータ
target_texts = [] #日本語のデータを格納
input_characters = set()    #英文に使われている文字の種類
target_characters = set()   #日本語に使われている文字の種類


with open(data_path, 'r', encoding='utf-8') as f:
    lines = f.read().split('\n')

for line in lines[: min(num_samples, len(lines) - 1)]:
    splited_text = line.split('\t')
    input_text = splited_text[0]
    target_text = splited_text[1]
    # ターゲット分の開始をタブ「\t」で、終了を改行「\n」で表す。
    target_text = '\t' + target_text + '\n'
    input_texts.append(input_text)
    target_texts.append(target_text)
    for char in input_text:
        if char not in input_characters:
            input_characters.add(char)
    for char in target_text:
        if char not in target_characters:
            target_characters.add(char)


for i in range(10):
    print(input_texts[i],":", target_texts[i])


#ソート
input_characters = sorted(list(input_characters))
target_characters = sorted(list(target_characters))
#インプット（英語）の単語数
num_encoder_tokens = len(input_characters)
#アウトプット（日本語）の単語数
num_decoder_tokens = len(target_characters)

#一番長い英文の数
max_encoder_seq_length = max([len(txt) for txt in input_texts])
#一番長い日本語分の数
max_decoder_seq_length = max([len(txt) for txt in target_texts])

print('input_texts:', len(input_texts))
print('num_encoder_tokens（英語）:', num_encoder_tokens)
print('num_decoder_tokens（日本語）:', num_decoder_tokens)
print('max_encoder_seq_length（英語）:', max_encoder_seq_length)
print('max_decoder_seq_length（日本語）:', max_decoder_seq_length)



#英語辞書データの生成
input_token_index = dict(
    [(char, i) for i, char in enumerate(input_characters)])
#日本語辞書データの生成
target_token_index = dict(
    [(char, i) for i, char in enumerate(target_characters)])

#ベクトルの入れ物を定義
#形状は (入力データ数,文章の最大長,トークン数)の3次元
# (10000, 22, 72) 英語
# (10000, 32, 1476) 日本語
encoder_input_data = np.zeros(
    (len(input_texts), max_encoder_seq_length, num_encoder_tokens),
    dtype='float32')
decoder_input_data = np.zeros(
    (len(input_texts), max_decoder_seq_length, num_decoder_tokens),
    dtype='float32')
decoder_target_data = np.zeros(
    (len(input_texts), max_decoder_seq_length, num_decoder_tokens),
    dtype='float32')

# インプットデータ（英語）、アウトプットデータ日本語）から1つずつ抽出して
# 辞書を使ってインデックスに変換して、one-Hot表現に変換

for i, (input_text, target_text) in enumerate(zip(input_texts, target_texts)):
    for t, char in enumerate(input_text):
        encoder_input_data[i, t, input_token_index[char]] = 1.
    for t, char in enumerate(target_text):

        decoder_input_data[i, t, target_token_index[char]] = 1.
        if t > 0:
            # decoder_target_dataは1タイムステップ進んでいるので正解データとしては1ステップ前の位置にセット。
            decoder_target_data[i, t - 1, target_token_index[char]] = 1.

print("英語の形状",encoder_input_data.shape)
print("日本語の形状",decoder_input_data.shape)


print("start dump")
test = 632
with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'wb') as p_test:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'wb') as p_test:
  pickle.dump(test, p_test)
print("finish dump test")
with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'wb') as p_num_encoder_tokens:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'wb') as p_num_encoder_tokens:
  pickle.dump(num_encoder_tokens, p_num_encoder_tokens)
print("finish dump num_encoder_tokens")
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'wb') as p_num_decoder_tokens:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'wb') as p_num_decoder_tokens:
  #pickle.dump(num_decoder_tokens, p_num_decoder_tokens)
print("finish dump num_decoder_tokens")
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'wb') as p_input_token_index:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'wb') as p_input_token_index:
  #pickle.dump(input_token_index, p_input_token_index)
print("finish dump input_token_index")
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'wb') as p_target_token_index:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'wb') as p_target_token_index:
  #pickle.dump(target_token_index, p_target_token_index)
print("finish dump")
"""
with open('data.binaryfile', 'wb') as :
  pickle.dump(, )

with open('data.binaryfile', 'wb') as :
  pickle.dump(, )
"""
with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'rb') as p_num_encoder_tokens:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'rb') as p_num_encoder_tokens:
  restored_num_encoder_tokens = pickle.load(p_num_encoder_tokens)

#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'rb') as p_num_decoder_tokens:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'rb') as p_num_decoder_tokens:
  #restored_num_decoder_tokens = pickle.load(p_num_decoder_tokens)

#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'rb') as p_input_token_index:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'rb') as p_input_token_index:
  #restored_input_token_index = pickle.load(p_input_token_index)

#with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'rb') as p_target_token_index:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'rb') as p_target_token_index:
  #restored_target_token_index = pickle.load(p_target_token_index)

with open('/home/ataka/Documents/reg/python/Seq2seq/data.binaryfile', 'rb') as p_test:
#with open('/home/ataka/Documents/reg/python/Seq2seq/data.dat', 'rb') as p_test:
  restored_test = pickle.load(p_test)


print("restored_test:")
print(restored_test)
print("num_encoder_tokens:")
print(num_encoder_tokens)
print("restored_num_encoder_tokens:")
#print(restored_num_encoder_tokens)
print("num_decoder_tokens:")
#print(num_decoder_tokens)
print("restored_num_encoder_tokens:")
#print(restored_num_encoder_tokens)





#assert restored_num_encoder_tokens == num_encoder_tokens
#assert restored_num_decoder_tokens == num_decoder_tokens
#assert restored_input_token_index == input_token_index
#assert restored_target_token_index == target_token_index

import dill
dill.dump_session('/home/ataka/Documents/reg/python/Seq2seq/session1.pkl')
