class UserImage(object):

    def __init__(self, encoded_image, image_embeddings=None):
        self.encoded_image = encoded_image
        self.image_embeddings = image_embeddings
