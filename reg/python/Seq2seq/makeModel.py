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

K.clear_session()



"""
with open('data.binaryfile', 'rb') as :
   = pickle.load()

with open('data.binaryfile', 'rb') as :
   = pickle.load()
"""

# encoderモデルの定義
# 入力長さは可変のためNone,トークン数=num_encoder_tokensを指定
encoder_inputs = Input(shape=(None, num_encoder_tokens),name="encoder_input")
encoder = LSTM(latent_dim, return_state=True,name="encoder_lstm")
encoder_outputs, state_h, state_c = encoder(encoder_inputs)
# encoderのアウトプットは不要なため内部状態２つのみを保持
encoder_states = [state_h, state_c]

#decoderの定義

decoder_inputs = Input(shape=(None, num_decoder_tokens),name="decoder_input")
# 初期状態としてencoderから出力された内部状態 encoder_statesセットする。
decoder_lstm = LSTM(latent_dim, return_sequences=True, return_state=True,name="decoder_lstm")
# 出力のみを取得
decoder_outputs, _, _ = decoder_lstm(decoder_inputs,
                                     initial_state=encoder_states)
#全結合層を定義しSoftmaxで単語の確率を出力するモデルを定義
decoder_dense = Dense(num_decoder_tokens, activation='softmax')
decoder_outputs = decoder_dense(decoder_outputs)

# encoder_input_dataとdecoder_input_dataを decoder_target_dataに変換するモデルを定義
model = Model([encoder_inputs, decoder_inputs], decoder_outputs)




# 学習

from keras.callbacks import EarlyStopping
# val_lossに改善が見られなくなってから、5エポックで学習は終了
early_stopping = EarlyStopping(monitor="val_loss", patience=3)

# 多クラス分類なのでcategorical_crossentropyを指定
model.compile(optimizer='rmsprop', loss='categorical_crossentropy')
history = model.fit([encoder_input_data, decoder_input_data], decoder_target_data,
          batch_size=batch_size,
          epochs=epochs,
          validation_split=0.2,
          callbacks=[early_stopping])

#モデルの保存
model.save('s2s.h5')


#matplotlib inline

#import matplotlib.pyplot as plt

loss = history.history['loss']
val_loss = history.history['val_loss']

#plt.plot(np.arange(len(loss)), loss)
#plt.plot(np.arange(len(val_loss)), val_loss)
#plt.show()


# encoderモデルの定義
from keras.models import load_model
model = load_model('s2s.h5', compile=False)

#encoder_inputsを入力、出力はencoderの内部状態であるencoder_statesを指定
#encoder_inputs～ encoder_states間にあるLSTM層は学習済みのものが利用される。
encoder_model = Model(encoder_inputs, encoder_states)

# decoderの定義
# #形状は中間層と同じlatent_dim=(256,)
decoder_state_input_h = Input(shape=(latent_dim,))
decoder_state_input_c = Input(shape=(latent_dim,))
#内部状態をリスト形式にして1つの変数にする。
decoder_states_inputs = [decoder_state_input_h, decoder_state_input_c]

# 既存の学習済みLSTM層(decoder_lstm)を使用し、初期状態にdecoder_states_inputsを指定
decoder_outputs, state_h, state_c = decoder_lstm(
    decoder_inputs, initial_state=decoder_states_inputs)
# decoderの内部状態２つをリストで結合
decoder_states = [state_h, state_c]

# 既存の学習済み全結合層(decoder_dense)を使用
decoder_outputs = decoder_dense(decoder_outputs)

# リストで結合
decoder_model = Model(
    [decoder_inputs] + decoder_states_inputs,
    [decoder_outputs] + decoder_states)

# モデルの保存
encoder_model.save('encoder_model.h5')
decoder_model.save('decoder_model.h5')
