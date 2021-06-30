import logging
from flask import Blueprint, request

from application.register_user import create_register_user, RegistrationRequest
from api.container import user_repository, face_net_model, vgg_face_model

registration_api = Blueprint('registration_bp', __name__)


def registration_impl(model):
    req_body = request.get_json()
    # print(req_body)
    registration_use_case = create_register_user(model, user_repository)
    try:
        registration_result = registration_use_case(RegistrationRequest(username=req_body['username'],
                                                                        encoded_images=req_body['images']))
    except Exception as e:
        logging.warning(e)
        registration_result = False
    return {
        "registrationSuccessful": registration_result
    }


@registration_api.post("/vggface")
def vgg_face_2fa():
    return registration_impl(vgg_face_model)


@registration_api.post("/facenet")
def face_net_2fa():
    return registration_impl(face_net_model)
