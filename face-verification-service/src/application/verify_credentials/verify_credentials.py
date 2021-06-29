from domain.entities import User
from domain.verification_models import VerificationModel
from domain.exceptions import InvalidVerificationCredentials, UsernameNotFoundException
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
            raise InvalidVerificationCredentials()
        self.is_base_64(request.encoded_image)

    @classmethod
    def is_base_64(cls, s: str):
        try:
            if "," not in s:
                raise InvalidVerificationCredentials()
            tokens = s.split(",")
            if len(tokens) != 2:
                raise InvalidVerificationCredentials()
            base64_str = tokens[1]
            if base64.b64encode(base64.b64decode(base64_str)).decode('ascii') != base64_str:
                raise InvalidVerificationCredentials()
        except Exception:
            raise InvalidVerificationCredentials()


class VerifyCredentials(UseCase):

    def __init__(self, verification_model: VerificationModel, validator: Validator):
        self._verification_model: VerificationModel = verification_model
        self._ensure_valid: Validator = validator

    def execute(self, request: VerifyCredentialsRequest):
        self._ensure_valid(request)
        user = User.objects(username=request.username).first()
        if not user:
            raise UsernameNotFoundException()
        return self._verification_model.verify(request.encoded_image, user)
