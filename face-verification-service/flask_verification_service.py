import logging
from logging.config import dictConfig
from flask import Flask, request, jsonify

rest_port = 8181
app = Flask(__name__)

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
        'level': 'DEBUG',
        'handlers': ['wsgi']
    }
})

@app.route("/vggface", methods=['POST'])
def vggface_2fa():
    logging.debug("=== serving vggface ===")
    data = request.get_json()
    logging.debug(data)
    return jsonify({
        'verificationSuccessfull': False
    })

@app.route("/facenet", methods=['POST'])
def facenet_2fa():
    logging.debug("=== serving facenet ===")
    data = request.get_json()
    logging.debug(data)
    return jsonify({
        'verificationSuccessfull': True
    })

if __name__ == "__main__":
    from waitress import serve
    logging.info("starting flask face verification service")
    serve(app, host='0.0.0.0', port = rest_port)
    logging.info("shutting down flask face verification service")