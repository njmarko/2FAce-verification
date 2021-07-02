from infrastructure.verification_models.keras_verification_model_base import KerasVerificationModelBase

import keras_facenet
import tensorflow as tf
import numpy as np

from random import choice


class FaceNet(KerasVerificationModelBase):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer, expected_shape=(160, 160))
        self._model = keras_facenet.FaceNet()

    def forward_image_pass(self, image):
        # keras_facenet implementation of FaceNet already does the sample wise normalization
        # so we don't need to do it explicitly
        return self._model.embeddings(image)

    def verify(self, encoded_image, user):
        label_image = np.frombuffer(choice(user.images).image_embeddings, dtype=np.float32).reshape(1, -1)
        verification_image = self._model.embeddings(self._image_to_tensor(encoded_image, self._expected_shape))
        user_model = self.load_user_specific_model(user, input_shape=verification_image.shape)
        model_prediction = user_model.predict(verification_image)
        embedding_distance = self._model.compute_distance(verification_image, label_image)
        return self.prediction_function(model_prediction, embedding_distance)

    def prediction_function(self, model_prediction, embedding_distance):
        print(f"User specific model prediction: {model_prediction}")
        print(f"Embedding distance: {embedding_distance}")
        print(f"FaceNet final prediction: {(model_prediction - 1.2 * embedding_distance)}")
        return True if (model_prediction - 1.2 * embedding_distance) >= 0.0 else False

    def get_transfer_learning_model(self):
        # transfer learning - attach 3 layers to pretrained FaceNet model
        for layer in self._model.model.layers:
            layer.trainable = False
        last_output = self._model.model.get_layer('normalize').output
        x = tf.keras.layers.Dense(name='D1', units=256, activation=tf.nn.tanh)(last_output)
        x = tf.keras.layers.Dropout(0.2)(x)
        x = tf.keras.layers.Dense(name='D2', units=1, activation=tf.nn.sigmoid)(x)

        model = tf.keras.Model(self._model.model.input, x)
        model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.01),
                      loss=tf.keras.losses.BinaryCrossentropy(),
                      metrics=[tf.keras.metrics.BinaryAccuracy(),
                               tf.keras.metrics.FalsePositives(),
                               tf.keras.metrics.FalseNegatives(),
                               tf.keras.metrics.TruePositives(),
                               tf.keras.metrics.TrueNegatives()])
        return model

    def register(self, user):
        train_x, train_y = self.get_train_data(user)
        # create a training dataset generator and configure data augmentation
        train_data_gen = self.get_training_data_generator()
        # train user specific verification model
        model = self.fit_user_specific_model(train_data_gen, train_x, train_y, batch_size=32,
                                             shuffle=True, epochs=2, steps_per_epoch=8)
        return self.serialize_model(model, input_shape=(1, 512))
