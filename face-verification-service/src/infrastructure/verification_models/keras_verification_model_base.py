import time

from application.common import FaceVerificationModel
from infrastructure.verification_models.user_specific_verification_model import UserSpecificVerificationModel
from abc import ABC, abstractmethod
import numpy as np
import tensorflow as tf
import datetime


class KerasVerificationModelBase(FaceVerificationModel, ABC):

    def __init__(self, image_to_tensor, model_serializer, expected_shape, similarity):
        super().__init__(image_to_tensor, model_serializer, expected_shape)
        self._similarity = similarity

    def get_train_data(self, user):
        correct_images = np.stack([self._image_to_tensor(image.encoded_image, self._expected_shape)
                                   for image in user.images])
        false_images = np.stack(self.load_false_images())
        train_y = np.concatenate((np.ones(len(correct_images)), np.zeros(len(false_images))), axis=0).reshape((-1, 1))
        train_x = np.concatenate((correct_images, false_images), axis=0).reshape((-1, *self._expected_shape, 3))
        print(train_x.shape)
        print(train_y.shape)
        return train_x, train_y

    @staticmethod
    def get_training_data_generator():
        return tf.keras.preprocessing.image.ImageDataGenerator(zoom_range=0.1,
                                                               width_shift_range=0.1,
                                                               height_shift_range=0.1,
                                                               shear_range=0.1,
                                                               rotation_range=20,
                                                               validation_split=0.2,
                                                               brightness_range=[0.2, 1.5],
                                                               rescale=1. / 255,
                                                               samplewise_center=True,
                                                               samplewise_std_normalization=True)

    def fit_user_specific_model(self, train_data_gen, train_x, train_y, batch_size=25,
                                shuffle=True, epochs=8, steps_per_epoch=2):
        model = self.get_transfer_learning_model()
        train_generator = train_data_gen.flow(train_x, train_y, batch_size=batch_size, shuffle=shuffle)
        train_dataset = tf.data.Dataset.from_generator(lambda: train_generator,
                                                       output_types=(tf.float32, tf.float32),
                                                       )
        train_dataset.shuffle(len(train_x)).batch(batch_size).prefetch(tf.data.AUTOTUNE).cache()
        log_dir = "logs/fit/" + datetime.datetime.now().strftime("%Y%m%d-%H%M%S")
        tensorboard_callback = tf.keras.callbacks.TensorBoard(log_dir=log_dir, histogram_freq=1)
        cb_early_stop = tf.keras.callbacks.EarlyStopping(monitor='binary_accuracy', mode="max", patience=3)
        t1 = time.time()
        model.fit(train_dataset,
                  epochs=epochs,
                  steps_per_epoch=steps_per_epoch,
                  callbacks=[tensorboard_callback, cb_early_stop])
        print(f"Vreme treniranja {time.time() - t1}")
        return model

    def serialize_model(self, model, input_shape, n_hidden=256):
        user_model = UserSpecificVerificationModel(n_hidden)
        user_model.train_model(input_shape=input_shape, model=model)
        return self._model_serializer.serialize(user_model.get_weights())

    def load_user_specific_model(self, user, input_shape, n_hidden=256):
        user_model = UserSpecificVerificationModel(n_hidden)
        user_model.build(input_shape=input_shape)
        user_model.set_weights(self._model_serializer.deserialize(user.verification_model))
        return user_model

    @abstractmethod
    def get_transfer_learning_model(self):
        pass
