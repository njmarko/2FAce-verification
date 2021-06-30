from domain.entities.image import UserImage
from mongoengine import Document, StringField, EmbeddedDocumentField, ListField, BinaryField


class User(Document):
    username = StringField(unique=True, max_length=50, required=True)
    images = ListField(EmbeddedDocumentField(UserImage))
    verification_model = BinaryField(required=True)
