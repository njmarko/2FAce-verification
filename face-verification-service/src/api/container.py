from infrastructure.persistence.repositories import UserRepository
from infrastructure.verification_models import FaceNet, VggFace
from infrastructure.image_processing import PILImageToNumpyArray
from infrastructure.serialization import PickleModelEncoder

pickle_encoder = PickleModelEncoder()

user_repository = UserRepository()

pil_image_to_np = PILImageToNumpyArray()
vgg_face_model = VggFace(pil_image_to_np, pickle_encoder)
face_net_model = FaceNet(pil_image_to_np, pickle_encoder)
