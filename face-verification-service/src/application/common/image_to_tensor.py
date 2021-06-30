from abc import ABC, abstractmethod


class ImageToTensor(ABC):

    @abstractmethod
    def convert(self, image, expected_shape):
        pass

    def __call__(self, image, expected_shape):
        return self.convert(image, expected_shape)
