from application.common import FaceVerificationModel
from keras_vggface.vggface import VGGFace


class VggFace(FaceVerificationModel):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer, expected_shape=(224, 224))
        self._model = VGGFace(model='resnet50', include_top=False, input_shape=(224, 224, 3), pooling='avg')

    def forward_image_pass(self, image):
        return self._model.predict(self._normalize(image))

    def verify(self, encoded_image, user):
        # TODO: util.preprocessing to center colors across the channels
        image = self._image_to_tensor(encoded_image)
        x = self.forward_image_pass(image)
        print(x.shape)
        return True

    def register(self, user):
        return True

    def _normalize(self, image):
        # TODO: Do sample wise std normalization and rescaling
        return image
