from domain.exceptions import InvalidVerificationCredentials
from application.common import UseCase, Validator
from dataclasses import dataclass
import base64


@dataclass
class VerifyCredentialsRequest(object):
    username: str
    encoded_image: str


class VerifyCredentialsRequestValidator(Validator):

    def validate(self, request: VerifyCredentialsRequest):
        if request.username is None or request.username.strip() == '':
            raise InvalidVerificationCredentials("Username can't be empty.")
        self.is_base_64(request.encoded_image)

    @classmethod
    def is_base_64(cls, s: str):
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


class VerifyCredentials(UseCase):

    def __init__(self, verification_model, user_repository, validator):
        self._verification_model = verification_model
        self._user_repository = user_repository
        self._ensure_valid = validator

    def execute(self, request):
        self._ensure_valid(request)
        user = self._user_repository.find_one(request.username)
        return self._verification_model.verify(request.encoded_image, user)
