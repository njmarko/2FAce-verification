from mongoengine import EmbeddedDocument, BinaryField, StringField


class DbUserImage(EmbeddedDocument):
    encoded_image = StringField(max_length=1000000, required=True)
    image_embeddings = BinaryField()
