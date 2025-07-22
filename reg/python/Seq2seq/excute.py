
from __future__ import print_function

from keras.models import Model
from keras.layers import Input, LSTM, Dense
import numpy as np
from keras import backend as K
#import makeData
import pickle
import dill
dill.load_session('/home/ataka/Documents/reg/python/Seq2seq/session1.pkl')

batch_size = 64  # バッチサイズ
epochs = 100  # エポックサイズ.
latent_dim = 256  # エンコーディングの次元数
num_samples = 10000  # サンプル数
# モデルのパス
#data_path = '/home/ataka/Documents/rep/python/Seq2seq/jpn.txt'
import sys

args = sys.argv
model_path = args[2] #"/home/ataka/Documents/rep/python/Seq2seq/"

K.clear_session()





from keras.models import load_model
encoder_model = load_model(model_path + 'encoder_model.h5', compile=False)
decoder_model = load_model(model_path + 'decoder_model.h5', compile=False)







reverse_input_char_index = dict(
    (i, char) for char, i in input_token_index.items())
reverse_target_char_index = dict(
    (i, char) for char, i in target_token_index.items())


def decode_sequence(input_seq):
    # 入力文(input_seq)を与えてencoderから内部状態を取得
    states_value = encoder_model.predict(input_seq)

    # 長さ1の空のターゲットシーケンスを生成
    target_seq = np.zeros((1, 1, num_decoder_tokens))
    # ターゲットシーケンスの最初の文字に開始文字であるタブ「\t」を入力
    target_seq[0, 0, target_token_index['\t']] = 1.

    # シーケンスのバッチのサンプリングループ
    # バッチサイズ1を想定
    stop_condition = False
    # 初期値として返答の文字列を空で作成。
    decoded_sentence = ''
    while not stop_condition:
        output_tokens, h, c = decoder_model.predict(
            [target_seq] + states_value)

        # トークンをサンプリングする

        # argmaxで最大確率のトークンインデックス番号を取得
        sampled_token_index = np.argmax(output_tokens[0, -1, :])
        # Index空文字を取得
        sampled_char = reverse_target_char_index[sampled_token_index]
        # 返答文字列にサンプリングされた文字を追加
        decoded_sentence += sampled_char

        # 終了条件：最大長に達するか停止文字を見つける。
        if (sampled_char == '\n' or
           len(decoded_sentence) > max_decoder_seq_length):
            stop_condition = True

        # ターゲット配列（長さ1）を更新
        # 長さ1の空のターゲットシーケンスを生成
        target_seq = np.zeros((1, 1, num_decoder_tokens))
        #予測されたトークンの値を1にセットし次の時刻の入力にtarget_seqを使う。
        target_seq[0, 0, sampled_token_index] = 1.

        # 内部状態を更新して次の時刻の入力に使う。
        states_value = [h, c]

    return decoded_sentence


import numpy as np
"""
for _ in range(10):

    seq_index = np.random.randint(len(input_texts))
    #encoderへの入力encoder_input_data（英語）から文章を1つ取得
    input_seq = encoder_input_data[seq_index: seq_index + 1]
    #入力文（英語）を日本語に翻訳
    decoded_sentence = decode_sequence(input_seq)
    print('-')
    print('Input sentence:', input_texts[seq_index])
    print('Decoded sentence:', decoded_sentence)
"""




#使用できない文字（コーパスにない文字）があったときに無効な文字を判定するために使う。
def is_invalid(message):
    is_invalid =False
    for char in message:
        if char not in input_characters:
            is_invalid = True
    return is_invalid

# 文章をone-hot表現に変換する関数
def sentence_to_vector(sentence):
    vector = np.zeros((1, max_encoder_seq_length, num_encoder_tokens))
    for j, char in enumerate(sentence):
        vector[0][j][input_token_index[char]] = 1
    return vector

"""

print("英文を入力してください。:")

message = ""
while message != "exit":

    while True:
        message = input()
        if not is_invalid(message):
            break
        else:
            print("英文を入力してください。")
    vec = sentence_to_vector(message)
    response = decode_sequence(vec)
    print(response)

"""


"""
print(len(args))
print(args[0])
print(args[1])
"""

"""

for i in range(len(args)-1):
    input_seq = args[i+1]
    print(input_seq)
    #decoded_sentence = decode_sequence(args[i+1])

    vec = sentence_to_vector(input_seq)
    response = decode_sequence(vec)
    print('---')
    print('Input sentence:', args[i+1])
    print('Decoded sentence:', response)

"""

input_seq = args[1]
print(input_seq)
#decoded_sentence = decode_sequence(args[i+1])
vec = sentence_to_vector(input_seq)
response = decode_sequence(vec)
print('---')
print('Input sentence:', input_seq)
print('Decoded sentence:', response)
