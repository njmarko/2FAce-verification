from abc import ABC, abstractmethod
import numpy as np
from PIL import Image
import base64
import io


class VerificationModel(ABC):
    @abstractmethod
    def verify(self, encoded_image, user):
        pass


class RegistrationModel(ABC):
    @abstractmethod
    def register(self, request):
        pass


class FaceVerificationModel(VerificationModel, RegistrationModel, ABC):
    pass


def to_numpy_array(image_string: str, expected_shape: tuple):
    if "," in image_string:
        image_string = image_string.split(",")[1]
    base64_decoded = base64.b64decode(image_string)
    image = Image.open(io.BytesIO(base64_decoded))
    image = image.resize(expected_shape)
    # image.show()
    # we need the image to be of shape (1, *expected_shape, 3)
    arr = np.array(image)[..., :3]  # cut off the fourth dimension which represents transparency
    return np.expand_dims(arr, axis=0)  # add one dimension as the first one
