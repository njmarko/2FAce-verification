from abc import ABC, abstractmethod


class VerificationModel(ABC):
    @abstractmethod
    def verify(self, encoded_image, user):
        pass


class RegistrationModel(ABC):
    @abstractmethod
    def register(self, request):
        pass


class FaceVerificationModel(VerificationModel, RegistrationModel, ABC):

    def __init__(self, image_to_tensor):
        self._image_to_tensor = image_to_tensor
