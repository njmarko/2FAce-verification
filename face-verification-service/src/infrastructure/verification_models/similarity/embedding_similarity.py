from abc import ABC, abstractmethod


class EmbeddingSimilarity(ABC):

    @abstractmethod
    def similarity(self, x, y):
        pass

    def __call__(self, x, y):
        return self.similarity(x, y)