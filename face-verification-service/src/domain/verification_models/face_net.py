from domain.verification_models import FaceVerificationModel, to_numpy_array
import keras_facenet


class FaceNet(FaceVerificationModel):

    def __init__(self):
        self._model = keras_facenet.FaceNet()

    def verify(self, encoded_image, user):
        label_image = self._model.embeddings(to_numpy_array(user.image.encoded_image, expected_shape=(160, 160)))
        verification_image = self._model.embeddings(to_numpy_array(encoded_image, expected_shape=(160, 160)))
        print(self._model.compute_distance(verification_image, label_image))
        return True if self._model.compute_distance(verification_image, label_image) < 1.0 else False

    def register(self, request):
        return True
