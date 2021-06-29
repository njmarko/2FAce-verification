from abc import ABC, abstractmethod


class UseCase(ABC):

    @abstractmethod
    def execute(self, request):
        pass

    def __call__(self, request):
        return self.execute(request)
