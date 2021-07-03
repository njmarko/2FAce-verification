from infrastructure.persistence.model.user_image import DbUserImage
from mongoengine import Document, StringField, EmbeddedDocumentField, ListField, BinaryField


class DbUser(Document):
    username = StringField(unique=True, max_length=50, required=True)
    images = ListField(EmbeddedDocumentField(DbUserImage))
    verification_model = BinaryField(required=True)
