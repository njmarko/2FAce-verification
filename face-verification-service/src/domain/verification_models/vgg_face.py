from domain.verification_models import FaceVerificationModel, to_numpy_array
from keras_vggface.vggface import VGGFace
from keras_vggface import utils


class VggFace(FaceVerificationModel):

    def __init__(self):
        self._model = VGGFace(model='resnet50', include_top=False, input_shape=(224, 224, 3), pooling='avg')

    def verify(self, encoded_image, user):
        # TODO: util.preprocessing to center colors across the channels
        x = self._model.predict(to_numpy_array(encoded_image, expected_shape=(224, 224)))
        print(x.shape)
        return True

    def register(self, request):
        return True
