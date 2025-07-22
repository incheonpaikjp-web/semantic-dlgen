'''
A Convolutional Network implementation example using TensorFlow library for Classification by TFIDF
Drama - Economy Domain /  Feature Size = 784

Author: Aymeric Damien, Edited by Incheon Paik
Project: https://github.com/aymericdamien/TensorFlow-Examples/
'''

import gzip
import os
import tempfile
import sys

import numpy
from six.moves import urllib
from six.moves import xrange  # pylint: disable=redefined-builtin

import tensorflow as tf

# Import MINST data
#mnist = input_data.read_data_sets("/tmp/data/", one_hot=True)

##################  For Reading Input Data  ############################
batch_size = 100 
#filename_queue_train = tf.train.string_input_producer(["/home/ataka/Documents/Impementation/data/learning_data/data_set/train_set/CNN/exchange_price_with_concept_CNN.csv.train"])
#filename_queue_test = tf.train.string_input_producer(["/home/ataka/Documents/Impementation/data/learning_data/data_set/test_set/CNN/exchange_price_with_concept_CNN.csv.test"])
#filename_queue_train = tf.train.string_input_producer(["/home/ataka/Documents/Impementation/ESA/Demo/exp/test2/CNN/result-train.csv"])
#filename_queue_test  = tf.train.string_input_producer(["/home/ataka/Documents/Impementation/ESA/Demo/exp/test2/CNN/result-test.csv"])
name = os.path.dirname(os.path.abspath(__name__))
#joined_path1 = os.path.join(name, 'result-train.csv')
joined_path1 = os.path.join(name, '/home/ataka/Documents/Impementation/ESA/Demo/exp/test3/IS_OS/data/result/Add/OUTPUT/result-train.csv')
data_path_train = os.path.normpath(joined_path1)
#joined_path2 = os.path.join(name, 'result-test.csv')
joined_path2 = os.path.join(name, '/home/ataka/Documents/Impementation/ESA/Demo/exp/test3/IS_OS/data/result/Add/OUTPUT/result-test.csv')
data_path_test = os.path.normpath(joined_path2)
filename_queue_train = tf.train.string_input_producer([data_path_train])
filename_queue_test  = tf.train.string_input_producer([data_path_test])



reader = tf.TextLineReader()
key1, trainValue = reader.read(filename_queue_train)
key2, testValue = reader.read(filename_queue_test)

# Default values, in case of empty columns. Also specifies the type of the
#default = [[0.0] for x in range(1446)]
default = [[0.0] for x in range(2027)]

##################  For Training Data  ############################
line_train = tf.decode_csv(trainValue, record_defaults=default, field_delim=",", name=None)
train_label_pack = tf.pack([line_train[0], line_train[1]])
#train_label_pack = tf.stack([line_train[0], line_train[1]])
train_feature_pack = tf.pack(list(line_train[2:]))
#train_feature_pack = tf.stack(list(line_train[2:]))
train_label_batch, train_feature_batch = tf.train.batch([train_label_pack, train_feature_pack], batch_size = batch_size, num_threads=1)

##################  For Test Data  ############################
line_test = tf.decode_csv(testValue, record_defaults=default)
test_label_pack = tf.pack([line_test[0], line_test[1]])
#test_label_pack = tf.stack([line_test[0], line_test[1]])
test_feature_pack = tf.pack(list(line_test[2:]))
#test_feature_pack = tf.stack(list(line_test[2:]))
test_label_batch, test_feature_batch = tf.train.batch([test_label_pack, test_feature_pack], batch_size = 312)
#test_label_batch, test_feature_batch = tf.train.batch([test_label_pack, test_feature_pack], batch_size = batch_size, num_threads=1)
####################  End of Creating Batches for Reading Input ######################

# Parameters
learning_rate = 0.001
#learning_rate = 0.0005
training_iters = 30000
#batch_size = 100
display_step = 10

# Network Parameters
#n_input = 1444 # MNIST data input (TFIDF value shape: 38*38)
n_input = 2025 # MNIST data input (TFIDF value shape: 45*45)
n_classes = 2 # Drama or Economy 
dropout = 1.0 # Dropout, probability to keep units

# tf Graph input
x = tf.placeholder(tf.float32, [None, n_input])
y = tf.placeholder(tf.float32, [None, n_classes])
keep_prob = tf.placeholder(tf.float32) #dropout (keep probability)

# Create model
def conv2d(img, w, b):
    return tf.nn.relu(tf.nn.bias_add(tf.nn.conv2d(img, w, strides=[1, 1, 1, 1], padding='SAME'),b))

def max_pool(img, k):
    return tf.nn.max_pool(img, ksize=[1, k, k, 1], strides=[1, k, k, 1], padding='SAME')

def conv_net(_X, _weights, _biases, _dropout):
    # Reshape input picture
    #_X = tf.reshape(_X, shape=[-1, 38, 38, 1])
    _X = tf.reshape(_X, shape=[-1, 45, 45, 1])

    # Convolution Layer
    conv1 = conv2d(_X, _weights['wc1'], _biases['bc1'])
    print('conv1: ')
    print (conv1)
    # Max Pooling (down-sampling)
    conv1 = max_pool(conv1, k=2)
    print('after maxpool: ')
    print (conv1)
    # Apply Dropout
    conv1 = tf.nn.dropout(conv1, _dropout)

    # Convolution Layer
    conv2 = conv2d(conv1, _weights['wc2'], _biases['bc2'])
    print('conv2: ')
    print (conv2)
    # Max Pooling (down-sampling)
    conv2 = max_pool(conv2, k=2)
    print('after maxpool: ')
    print (conv2)
    # Apply Dropout
    conv2 = tf.nn.dropout(conv2, _dropout)

    # Fully connected layer
    dense1 = tf.reshape(conv2, [-1, _weights['wd1'].get_shape().as_list()[0]]) # Reshape conv2 output to fit dense layer input
    dense1 = tf.nn.relu(tf.add(tf.matmul(dense1, _weights['wd1']), _biases['bd1'])) # Relu activation
    dense1 = tf.nn.dropout(dense1, _dropout) # Apply Dropout

    # Output, class prediction
    out = tf.add(tf.matmul(dense1, _weights['out']), _biases['out'])
    return out

# Store layers weight & bias
weights = {
    #'wc1': tf.Variable(tf.random_normal([5, 5, 1, 38])), # 5x5 conv, 1 input, 32 outputs
    #'wc2': tf.Variable(tf.random_normal([19, 19, 38, 64])), # 5x5 conv, 32 inputs, 64 outputs
    #'wd1': tf.Variable(tf.random_normal([10*10*64, 1444])), # fully connected, 7*7*64 inputs, 1024 outputs
    #'out': tf.Variable(tf.random_normal([1444, n_classes])) # 1024 inputs, 10 outputs (class prediction)
    'wc1': tf.Variable(tf.random_normal([5, 5, 1, 45])), # 5x5 conv, 1 input, 32 outputs
    'wc2': tf.Variable(tf.random_normal([19, 19, 45, 64])), # 5x5 conv, 32 inputs, 64 outputs
    'wd1': tf.Variable(tf.random_normal([12*12*64, 2025])), # fully connected, 7*7*64 inputs, 1024 outputs
    'out': tf.Variable(tf.random_normal([2025, n_classes])) # 1024 inputs, 10 outputs (class prediction)
}

biases = {
    #'bc1': tf.Variable(tf.random_normal([38])),
    #'bc2': tf.Variable(tf.random_normal([64])),
    #'bd1': tf.Variable(tf.random_normal([1444])),
    #'out': tf.Variable(tf.random_normal([n_classes]))
    'bc1': tf.Variable(tf.random_normal([45])),
    'bc2': tf.Variable(tf.random_normal([64])),
    'bd1': tf.Variable(tf.random_normal([2025])),
    'out': tf.Variable(tf.random_normal([n_classes]))
}

# Construct model
pred = conv_net(x, weights, biases, keep_prob)

# Define loss and optimizer
cost = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(pred, y))
optimizer = tf.train.AdamOptimizer(learning_rate=learning_rate).minimize(cost)

# Evaluate model
correct_pred = tf.equal(tf.argmax(pred,1), tf.argmax(y,1))
accuracy = tf.reduce_mean(tf.cast(correct_pred, tf.float32))

# Initializing the variables
init = tf.initialize_all_variables()

# Launch the graph
with tf.Session() as sess:
  sess.run(init)
  coord = tf.train.Coordinator() 
  threads = tf.train.start_queue_runners(sess, coord=coord)
  #tf.train.start_queue_runners(sess)
  step = 1

  try: 
    # Keep training until reach max iterations
    while step * batch_size < training_iters:
        #batch_xs, batch_ys = mnist.train.next_batch(batch_size)
        train_label, train_feature = sess.run([train_label_batch, train_feature_batch])
        #test_label, test_feature = sess.run([test_label_batch, test_feature_batch])
        # Fit training using batch data
        sess.run(optimizer, feed_dict={x: train_feature, y: train_label, keep_prob: dropout})
        if step % display_step == 0:
            # Calculate batch accuracy
            acc = sess.run(accuracy, feed_dict={x: train_feature, y: train_label, keep_prob: 1.})
            # Calculate batch loss
            loss = sess.run(cost, feed_dict={x: train_feature, y: train_label, keep_prob: 1.})
            print ('Iter ' + str(step*batch_size) + ', Minibatch Loss= ' + '{:.6f}'.format(loss) + ', Training Accuracy= ' + '{:.5f}'.format(acc))
        step += 1

    print ('Optimization Finished!')
    # Calculate accuracy for TFIDF data
    test_label, test_feature = sess.run([test_label_batch, test_feature_batch])
    print ('Testing Accuracy: ', sess.run(accuracy, feed_dict={x: test_feature, y: test_label, keep_prob: 1.}))
    #print ('Testing Accuracy: ', sess.run(accuracy, feed_dict={x: test_feature[:400], y: test_label[:400], keep_prob: 1.}))
  finally:
    coord.request_stop()
  coord.join(threads)

