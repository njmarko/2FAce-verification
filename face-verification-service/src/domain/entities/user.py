class User(object):

    def __init__(self, username, images=None, verification_model=None):
        self.username = username
        self.images = images or []
        self.verification_model = verification_model
