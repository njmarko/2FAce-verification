from infrastructure.persistence.repositories import UserRepository
from infrastructure.verification_models import FaceNet, VggFace
from infrastructure.image_processing import PILImageToNumpyArray
from infrastructure.serialization import PickleModelSerializer

pickle_serializer = PickleModelSerializer()

user_repository = UserRepository()

pil_image_to_np = PILImageToNumpyArray()
vgg_face_model = VggFace(pil_image_to_np, pickle_serializer)
face_net_model = FaceNet(pil_image_to_np, pickle_serializer)
