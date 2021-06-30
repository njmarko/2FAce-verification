from domain.exceptions import InvalidVerificationCredentials
from application.common import UseCase, Validator, is_base_64
from dataclasses import dataclass


@dataclass
class VerifyCredentialsRequest(object):
    username: str
    encoded_image: str


class VerifyCredentialsRequestValidator(Validator):

    def validate(self, request: VerifyCredentialsRequest):
        if request.username is None or request.username.strip() == '':
            raise InvalidVerificationCredentials("Username can't be empty.")
        is_base_64(request.encoded_image)


class VerifyCredentials(UseCase):

    def __init__(self, verification_model, user_repository, validator):
        self._verification_model = verification_model
        self._user_repository = user_repository
        self._ensure_valid = validator

    def execute(self, request):
        self._ensure_valid(request)
        user = self._user_repository.find_one(request.username)
        return self._verification_model.verify(request.encoded_image, user)
