from abc import ABC, abstractmethod


class VerificationModel(ABC):
    @abstractmethod
    def verify(self, encoded_image, user):
        pass


class RegistrationModel(ABC):
    @abstractmethod
    def register(self, user):
        pass


class FaceVerificationModel(VerificationModel, RegistrationModel, ABC):

    def __init__(self, image_to_tensor, model_serializer):
        self._image_to_tensor = image_to_tensor
        self._model_serializer = model_serializer

    def load_false_images(self, expected_shape):
        return []
