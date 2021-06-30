import tensorflow as tf


class UserSpecificVerificationModel(tf.keras.Model):

    def __init__(self):
        super(UserSpecificVerificationModel, self).__init__()
        self.dense1 = tf.keras.layers.Dense(24, activation=tf.nn.relu)
        self.dense2 = tf.keras.layers.Dense(1, activation=tf.nn.sigmoid)

    def call(self, inputs, training=None, mask=None):
        x = self.dense1(inputs)
        return self.dense2(x)

    def get_config(self):
        return super(UserSpecificVerificationModel, self).get_config()
