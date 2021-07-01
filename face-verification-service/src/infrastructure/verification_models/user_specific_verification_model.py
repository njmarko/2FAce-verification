import tensorflow as tf


class UserSpecificVerificationModel(tf.keras.Model):

    def __init__(self):
        super(UserSpecificVerificationModel, self).__init__()
        self.dense1 = tf.keras.layers.Dense(units=256, activation=tf.nn.tanh, name='D1')
        self.dense2 = tf.keras.layers.Dense(units=1, activation=tf.nn.sigmoid, name='D2')

    def call(self, inputs, training=None, mask=None):
        x = self.dense1(inputs)
        return self.dense2(x)

    def train_model(self, input_shape, model):
        self.build(input_shape=input_shape)
        for layer in self.layers:
            layer.set_weights(model.get_layer(layer.name).get_weights())

    def get_config(self):
        return super(UserSpecificVerificationModel, self).get_config()
