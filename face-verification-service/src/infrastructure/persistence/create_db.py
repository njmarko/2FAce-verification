from mongoengine import connect


def create_db():
    connect(db='face_verification_service_db', host='127.0.0.1', port=27017)
