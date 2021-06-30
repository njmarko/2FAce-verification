from application.common import FaceVerificationModel
from infrastructure.verification_models.user_specific_verification_model import UserSpecificVerificationModel
import keras_facenet


class FaceNet(FaceVerificationModel):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer)
        self._model = keras_facenet.FaceNet()
        self._expected_shape = (160, 160)

    def verify(self, encoded_image, user):
        label_image = self._model.embeddings(self._image_to_tensor(user.images[0].encoded_image, self._expected_shape))
        verification_image = self._model.embeddings(self._image_to_tensor(encoded_image, self._expected_shape))
        user_model = UserSpecificVerificationModel()
        user_model.set_weights(self._model_serializer.deserialize(user.verification_model))
        user_model.build(input_shape=verification_image.shape)
        # TODO: Do the verification
        print(user_model.predict(verification_image))
        print(user_model.summary())
        print(self._model.compute_distance(verification_image, label_image))
        return True if self._model.compute_distance(verification_image, label_image) < 0.5 else False

    def register(self, user):
        correct_images = [self._image_to_tensor(image.encoded_image, self._expected_shape) for image in user.images]
        false_images = self.load_false_images(self._expected_shape)
        user_model = UserSpecificVerificationModel()
        user_model.compile(optimizer='adam')
        # TODO: Train the model
        return self._model_serializer.serialize(user_model.get_weights())

