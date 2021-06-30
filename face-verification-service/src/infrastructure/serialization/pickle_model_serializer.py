import pickle
from application.common import ModelSerializer


class PickleModelEncoder(ModelSerializer):

    def serialize(self, model):
        return pickle.dumps(model, protocol=pickle.HIGHEST_PROTOCOL)

    def deserialize(self, model):
        return pickle.loads(model)
