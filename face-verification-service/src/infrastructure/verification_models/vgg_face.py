import datetime
import itertools
import sys
import time

from keras import Model, Input
from keras.models import Sequential
from tensorflow import keras

from infrastructure.verification_models.keras_verification_model_base import KerasVerificationModelBase
from keras_vggface.vggface import VGGFace
from keras_vggface.utils import preprocess_input
import tensorflow as tf

from random import choice
import numpy as np


class VggFace(KerasVerificationModelBase):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer, expected_shape=(224, 224))
        self._model = VGGFace(model='resnet50', include_top=False, input_shape=(224, 224, 3), pooling='avg')

    def forward_image_pass(self, image):
        processed_image = preprocess_input(image.astype('float64'), version=2)
        return self._model.predict(processed_image)

    def verify(self, encoded_image, user):
        label_image = np.frombuffer(choice(user.images).image_embeddings, dtype=np.float32).reshape(1, -1)
        verification_image = self.forward_image_pass(self._image_to_tensor(encoded_image, self._expected_shape))
        user_model = self.load_user_specific_model(user, input_shape=verification_image.shape, n_hidden=512)
        model_prediction = user_model.predict(verification_image)
        # print(f"Vgg Model prediciton {model_prediction}")
        # cosine similarity
        embedding_distance = 1 - np.dot(label_image, verification_image.T) / (np.linalg.norm(label_image) * np.linalg.norm(verification_image))
        print(f"VggFace predicted: {embedding_distance}")

        return True if embedding_distance <= 0.5 else False

    def get_transfer_learning_model(self):
        model = tf.keras.Sequential()
        model.add(tf.keras.layers.Dense(512, activation=tf.nn.tanh, name="D1", input_shape=(None, 2048)))
        model.add(tf.keras.layers.Dropout(0.2))
        model.add(tf.keras.layers.Dense(units=1, activation=tf.nn.sigmoid, name="D2"))
        model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
                      loss=tf.keras.losses.BinaryCrossentropy(),
                      metrics=[tf.keras.metrics.BinaryAccuracy(),
                               tf.keras.metrics.FalsePositives(),
                               tf.keras.metrics.FalseNegatives(),
                               tf.keras.metrics.TruePositives(),
                               tf.keras.metrics.TrueNegatives()])
        return model

    def register(self, user):
        train_x, train_y = self.get_train_data(user)
        print(f"Train x shape {train_x.shape}, train y shape {train_y.shape}")
        true_face = np.frombuffer(choice(user.images).image_embeddings, dtype=np.float32).reshape(1, -1)
        model = self.get_transfer_learning_model()
        train_data_gen = self.get_training_data_generator()
        train_generator = train_data_gen.flow(train_x, train_y, batch_size=25, shuffle=True)

        t0 = time.time()
        dataset = [(self._model.predict(img), label)
                   for img, label in itertools.islice(train_generator, 0, 8)]
        train_labels = np.concatenate(tuple([x[1] for x in dataset]), axis=0).reshape(-1, 1)

        train_encodings = np.concatenate(tuple([x[0] for x in dataset]), axis=0).reshape(-1, 2048)
        print(f"Vreme pripreme podataka {time.time() - t0}")

        log_dir = "logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
        tensorboard_callback = tf.keras.callbacks.TensorBoard(log_dir=log_dir, histogram_freq=1)
        t1 = time.time()
        model.fit(x=train_encodings,
                  y=train_labels,
                  epochs=100,
                  shuffle=True,
                  steps_per_epoch=20,
                  callbacks=[tensorboard_callback])
        print(f"Vreme treniranja {time.time() - t1}")
        return self.serialize_model(model, input_shape=(1, 2048), n_hidden=512)
