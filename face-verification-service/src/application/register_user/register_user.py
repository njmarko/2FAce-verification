from typing import List
from domain.exceptions import InvalidVerificationCredentials
from domain.entities import User, UserImage
from application.common import UseCase, Validator, is_base_64
from dataclasses import dataclass


@dataclass
class RegistrationRequest(object):
    username: str
    encoded_images: List[str]


class RegistrationRequestValidator(Validator):

    def validate(self, request: RegistrationRequest):
        if request.username is None or request.username.strip() == '':
            raise InvalidVerificationCredentials("Username can't be empty.")
        if not request.encoded_images:
            raise InvalidVerificationCredentials("Images can't be empty.")
        for image in request.encoded_images:
            is_base_64(image)


class RegisterUser(UseCase):

    def __init__(self, registration_model, user_repository, validator):
        self._registration_model = registration_model
        self._user_repository = user_repository
        self._ensure_valid = validator

    def execute(self, request):
        self._ensure_valid(request)
        user = User(username=request.username,
                    images=[UserImage(encoded_image=image) for image in request.encoded_images])
        return self._user_repository.save(user) is not None
