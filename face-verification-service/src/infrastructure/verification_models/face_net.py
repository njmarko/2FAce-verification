import numpy as np

from application.common import FaceVerificationModel
from infrastructure.verification_models.user_specific_verification_model import UserSpecificVerificationModel
import keras_facenet
import tensorflow as tf


class FaceNet(FaceVerificationModel):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer)
        self._model = keras_facenet.FaceNet()
        self._expected_shape = (160, 160)

    def verify(self, encoded_image, user):
        label_image = self._model.embeddings(self._image_to_tensor(user.images[0].encoded_image, self._expected_shape))
        verification_image = self._model.embeddings(self._image_to_tensor(encoded_image, self._expected_shape))
        user_model = UserSpecificVerificationModel()
        user_model.build(input_shape=verification_image.shape)
        user_model.set_weights(self._model_serializer.deserialize(user.verification_model))
        # TODO: Do the verification
        print(user_model.predict(verification_image))
        print(self._model.compute_distance(verification_image, label_image))
        return True if self._model.compute_distance(verification_image, label_image) < 0.5 else False

    def register(self, user):
        correct_images = np.stack([self._image_to_tensor(image.encoded_image, self._expected_shape)
                                   for image in user.images])
        false_images = np.stack(self.load_false_images(self._expected_shape))
        train_y = np.concatenate((np.ones(len(correct_images)), np.zeros(len(false_images))), axis=0).reshape((-1, 1))
        train_x = np.concatenate((correct_images, false_images), axis=0).reshape((-1, 160, 160, 3))
        print(train_x.shape)
        print(train_y.shape)
        train_datagen = tf.keras.preprocessing.image.ImageDataGenerator(zoom_range=0.1,
                                                                        width_shift_range=0.1,
                                                                        height_shift_range=0.1,
                                                                        shear_range=0.1,
                                                                        rotation_range=20,
                                                                        validation_split=0.2,
                                                                        brightness_range=[0.2, 1.5],
                                                                        rescale=1. / 255,
                                                                        samplewise_center=True,
                                                                        samplewise_std_normalization=True)
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
                               tf.keras.metrics.TruePositives()])
        train_generator = train_datagen.flow(train_x, train_y, batch_size=100, shuffle=True)
        train_dataset = tf.data.Dataset.from_generator(lambda: train_generator, (tf.float32, tf.float32))
        train_dataset.prefetch(tf.data.AUTOTUNE).cache().batch(100)
        model.fit(train_dataset, epochs=5, steps_per_epoch=2)
        user_model = UserSpecificVerificationModel()
        user_model.train_model(input_shape=(1, 512), model=model)
        return self._model_serializer.serialize(user_model.get_weights())
