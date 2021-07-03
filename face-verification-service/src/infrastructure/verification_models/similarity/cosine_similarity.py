from infrastructure.verification_models.similarity.embedding_similarity import EmbeddingSimilarity
import numpy as np


class CosineSimilarity(EmbeddingSimilarity):

    def similarity(self, x, y):
        return 1 - np.dot(x, y.T) / (np.linalg.norm(x) * np.linalg.norm(y))
