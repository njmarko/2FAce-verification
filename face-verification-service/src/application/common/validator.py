import base64
from abc import ABC, abstractmethod

from domain.exceptions import InvalidVerificationCredentials


class Validator(ABC):

    @abstractmethod
    def validate(self, obj):
        pass

    def __call__(self, obj):
        self.validate(obj)


def is_base_64(s: str):
    try:
        base64_str = s
        if "," in s:
            tokens = s.split(",")
            if len(tokens) != 2:
                raise InvalidVerificationCredentials("Invalid base64 encoded image..")
            base64_str = tokens[1]
        if base64.b64encode(base64.b64decode(base64_str)).decode('ascii') != base64_str:
            raise InvalidVerificationCredentials("Invalid base64 encoded image.")
    except Exception:
        raise InvalidVerificationCredentials("Invalid base64 encoded image.")
