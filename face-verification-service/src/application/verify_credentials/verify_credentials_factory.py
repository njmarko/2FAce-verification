from application.verify_credentials.verify_credentials import VerifyCredentials, VerifyCredentialsRequestValidator


def create_verify_credentials(verification_model, user_repository):
    return VerifyCredentials(verification_model, user_repository, VerifyCredentialsRequestValidator())
