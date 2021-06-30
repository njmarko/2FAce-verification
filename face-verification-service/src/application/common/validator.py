from abc import ABC, abstractmethod


class Validator(ABC):

    @abstractmethod
    def validate(self, obj):
        pass

    def __call__(self, obj):
        self.validate(obj)
