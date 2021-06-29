from domain.entities.image import UserImage
from mongoengine import Document, StringField, EmbeddedDocumentField


class User(Document):
    username = StringField(unique=True, max_length=50, required=True)
    image = EmbeddedDocumentField(UserImage)
