import logging
from flask import Blueprint, request

from application.verify_credentials import create_verify_credentials, VerifyCredentialsRequest
from api.container import user_repository, face_net_model, vgg_face_model

verification_api = Blueprint('verification_bp', __name__)


def verification_impl(model):
    req_body = request.get_json()
    # print(req_body)
    verify_credentials_use_case = create_verify_credentials(model, user_repository)
    try:
        verification_result = verify_credentials_use_case(VerifyCredentialsRequest(username=req_body['username'],
                                                                                   encoded_image=req_body['image']))
    except Exception as e:
        logging.warning(e)
        verification_result = False
    return {
        "verificationSuccessful": verification_result
    }


@verification_api.post("/vggface")
def vgg_face_2fa():
    return verification_impl(vgg_face_model)


@verification_api.post("/facenet")
def face_net_2fa():
    return verification_impl(face_net_model)
