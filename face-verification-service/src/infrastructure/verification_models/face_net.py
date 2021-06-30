from application.common import FaceVerificationModel
import keras_facenet


class FaceNet(FaceVerificationModel):

    def __init__(self, image_to_tensor):
        super().__init__(image_to_tensor)
        self._model = keras_facenet.FaceNet()

    def verify(self, encoded_image, user):
        label_image = self._model.embeddings(self._image_to_tensor(user.images[0].encoded_image,
                                                                   expected_shape=(160, 160)))
        verification_image = self._model.embeddings(self._image_to_tensor(encoded_image,
                                                                          expected_shape=(160, 160)))
        print(self._model.compute_distance(verification_image, label_image))
        return True if self._model.compute_distance(verification_image, label_image) < 0.5 else False

    def register(self, request):
        return True
