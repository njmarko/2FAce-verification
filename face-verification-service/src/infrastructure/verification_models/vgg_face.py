from infrastructure.verification_models.keras_verification_model_base import KerasVerificationModelBase
from keras_vggface.vggface import VGGFace
from keras_vggface.utils import preprocess_input

from random import choice
import numpy as np


class VggFace(KerasVerificationModelBase):

    def __init__(self, image_to_tensor, model_serializer):
        super().__init__(image_to_tensor, model_serializer, expected_shape=(224, 224))
        self._model = VGGFace(model='resnet50', include_top=False, input_shape=(224, 224, 3), pooling='avg')

    def forward_image_pass(self, image):
        processed_image = preprocess_input(image.astype('float64'), version=2)
        return self._model.predict(processed_image)

    def verify(self, encoded_image, user):
        label_image = np.frombuffer(choice(user.images).image_embeddings, dtype=np.float32).reshape(1, -1)
        verification_image = self.forward_image_pass(self._image_to_tensor(encoded_image, self._expected_shape))
        # cosine similarity
        embedding_distance = 1 - np.dot(label_image, verification_image.T) / (np.linalg.norm(label_image) * np.linalg.norm(verification_image))
        print(f"VggFace predicted: {embedding_distance}")
        return True if embedding_distance <= 0.5 else False

    def get_transfer_learning_model(self):
        # not doing this right now
        return None

    def register(self, user):
        return bytes('123'.encode('ascii'))
