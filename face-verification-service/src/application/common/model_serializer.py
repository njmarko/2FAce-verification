from abc import ABC, abstractmethod


class ModelSerializer(ABC):

    @abstractmethod
    def serialize(self, model):
        pass

    @abstractmethod
    def deserialize(self, model):
        pass
