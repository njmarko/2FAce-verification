from logging.config import dictConfig

from infrastructure.persistence import seed_db
from infrastructure.persistence import create_db


def setup_logging():
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


def start_host():
    create_db()
    seed_db()
