from application.common import FaceVerificationModel
from keras_vggface.vggface import VGGFace


class VggFace(FaceVerificationModel):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer)
        self._model = VGGFace(model='resnet50', include_top=False, input_shape=(224, 224, 3), pooling='avg')

    def verify(self, encoded_image, user):
        # TODO: util.preprocessing to center colors across the channels
        x = self._model.predict(self._image_to_tensor(encoded_image, expected_shape=(224, 224)))
        print(x.shape)
        return True

    def register(self, user):
        return True
