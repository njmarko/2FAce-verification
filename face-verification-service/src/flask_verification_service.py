import logging
from logging.config import dictConfig
from flask import Flask, request

from infrastructure.persistence import seed_db
from infrastructure.persistence import create_db
from infrastructure.persistence.repositories import UserRepository
from infrastructure.verification_models import FaceNet, VggFace
from infrastructure.image_processing import PILImageToNumpyArray

from application.verify_credentials import create_verify_credentials, VerifyCredentialsRequest, VerifyCredentials

rest_port = 8181
app = Flask(__name__)
create_db()

dictConfig({
    'version': 1,
    'formatters': {'default': {
        'format': '[%(asctime)s] %(module)s %(levelname)s: %(message)s',
    }},
    'handlers': {'wsgi': {
        'class': 'logging.StreamHandler',
        'stream': 'ext://flask.logging.wsgi_errors_stream',
        'formatter': 'default'
    }},
    'root': {
        'level': 'INFO',
        'handlers': ['wsgi']
    }
})

user_repository = UserRepository()


def verification_impl(model):
    req_body = request.get_json()
    # print(req_body)
    verify_credentials_use_case: VerifyCredentials = create_verify_credentials(model, user_repository)
    try:
        verification_result = verify_credentials_use_case(VerifyCredentialsRequest(username=req_body['username'],
                                                                                   encoded_image=req_body['image']))
    except Exception as e:
        logging.warning(e)
        verification_result = False
    return {
        "verificationSuccessful": verification_result
    }


@app.post("/vggface")
def vgg_face_2fa():
    return verification_impl(VggFace(PILImageToNumpyArray()))


@app.post("/facenet")
def face_net_2fa():
    return verification_impl(FaceNet(PILImageToNumpyArray()))


if __name__ == "__main__":
    from waitress import serve
    logging.info("starting flask face verification service")
    seed_db()
    serve(app, host='0.0.0.0', port=rest_port)
    logging.info("shutting down flask face verification service")
