import logging
import argparse
from flask import Flask
from api import setup_logging, start_host
from api import verification_api, registration_api
from py_eureka_client import eureka_client

EUREKA_SERVER = "http://localhost:8761/eureka"
DEFAULT_PORT = 8181
SERVICE_NAME = "face-verification-service"

parser = argparse.ArgumentParser(description='Face verification service instance.')
parser.add_argument('-p', '--port', nargs='?', default=DEFAULT_PORT, type=int,
                    help='Port on which the current instance will run.')

setup_logging()

app = Flask(__name__)

app.register_blueprint(verification_api, url_prefix='/')
app.register_blueprint(registration_api, url_prefix='/register')


def get_port():
    args = parser.parse_args()
    return args.port


def run_service():
    port = get_port()
    eureka_client.init(eureka_server=EUREKA_SERVER, app_name=SERVICE_NAME, instance_port=port)
    from waitress import serve
    logging.info("starting flask face verification service")
    start_host()
    serve(app, host='0.0.0.0', port=port)
    logging.info("shutting down flask face verification service")


if __name__ == "__main__":
    run_service()
