from application.common import ImageToTensor
import numpy as np
from PIL import Image
import base64
import io


class PILImageToNumpyArray(ImageToTensor):

    def convert(self, image, expected_shape):
        if "," in image:
            image = image.split(",")[1]
        base64_decoded = base64.b64decode(image)
        image = Image.open(io.BytesIO(base64_decoded))
        # image.show()
        image = image.resize(expected_shape)
        # we need the image to be of shape (1, *expected_shape, 3)
        arr = np.array(image)[..., :3]  # cut off the fourth dimension which represents transparency
        return np.expand_dims(arr, axis=0)  # add one dimension as the first one
