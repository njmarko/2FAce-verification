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
        # print(user_model.summary())
        print(self._model.compute_distance(verification_image, label_image))
        return True if self._model.compute_distance(verification_image, label_image) < 0.5 else False

    def register(self, user):
        correct_images = np.stack([self._image_to_tensor(image.encoded_image, self._expected_shape)
                                   for image in user.images])
        # correct_embeddings = [self._model.embeddings(image) for image in correct_images]
        # correct_x = np.stack(correct_embeddings)
        false_images = np.stack(self.load_false_images(self._expected_shape))
        # false_embeddings = [self._model.embeddings(image) for image in false_images]
        # false_x = np.stack(false_embeddings)
        # train_x = np.concatenate((correct_x, false_x), axis=0)
        train_y = np.concatenate((np.ones(len(correct_images)), np.zeros(len(false_images))), axis=0).reshape((-1, 1))
        train_x = np.concatenate((correct_images, false_images), axis=0).reshape((-1, 160, 160, 3))
        print(train_x.shape)
        print(train_y.shape)
        train_datagen = tf.keras.preprocessing.image.ImageDataGenerator(zoom_range=0.1,
                                                                        width_shift_range=0.1,
                                                                        height_shift_range=0.1,
                                                                        shear_range=0.1,
                                                                        rotation_range=20)
        for layer in self._model.model.layers:
            layer.trainable = False
        last_output = self._model.model.get_layer('normalize').output
        x = tf.keras.layers.Dense(name='D1', units=24, activation=tf.nn.relu)(last_output)
        x = tf.keras.layers.Dense(name='D2', units=1, activation=tf.nn.sigmoid)(x)

        model = tf.keras.Model(self._model.model.input, x)
        model.compile(optimizer=tf.keras.optimizers.Adam(), loss='binary_crossentropy', metrics=['acc'])
        # print(self._model.model.summary())
        # print(model.summary())
        train_generator = train_datagen.flow(train_x, train_y, batch_size=2, shuffle=True)
        model.fit(train_generator, epochs=50)
        # print(model.summary())
        # print(self._model.model.layers)
        user_model = UserSpecificVerificationModel()
        user_model.train_model(input_shape=(1, 512), model=model)
        # user_model.fit_generator(train_generator, epochs=100)
        # TODO: Train the model
        # print(user_model.get_weights())
        return self._model_serializer.serialize(user_model.get_weights())
