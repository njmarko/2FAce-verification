from domain.verification_models import VerificationModel
from application.verify_credentials.verify_credentials import VerifyCredentials, VerifyCredentialsRequestValidator


def create_verify_credentials(verification_model: VerificationModel):
    return VerifyCredentials(verification_model, VerifyCredentialsRequestValidator())
