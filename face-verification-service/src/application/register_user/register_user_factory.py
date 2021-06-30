from application.register_user.register_user import RegisterUser, RegistrationRequestValidator


def create_register_user(registration_model, user_repository):
    return RegisterUser(registration_model, user_repository, RegistrationRequestValidator())
