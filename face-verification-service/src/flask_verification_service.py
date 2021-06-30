import logging
from flask import Flask
from api import setup_logging, start_host
from api import verification_api, registration_api

setup_logging()

rest_port = 8181
app = Flask(__name__)

app.register_blueprint(verification_api, url_prefix='/')
app.register_blueprint(registration_api, url_prefix='/register')

if __name__ == "__main__":
    from waitress import serve
    logging.info("starting flask face verification service")
    start_host()
    serve(app, host='0.0.0.0', port=rest_port)
    logging.info("shutting down flask face verification service")
