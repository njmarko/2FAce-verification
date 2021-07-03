from domain.entities import User, UserImage
from infrastructure.persistence.model import DbUser, DbUserImage
from domain.exceptions import UsernameNotFoundException


class UserRepository(object):

    def find_one(self, username):
        user = DbUser.objects(username=username).first()
        if not user:
            raise UsernameNotFoundException(f"Cannot find user with username {username}.")
        return self._to_domain_user(user)

    def save(self, user):
        db_user = self._to_db_user(user)
        db_user.save()
        return user

    @classmethod
    def _to_domain_user(cls, db_user):
        return User(
            username=db_user.username,
            images=[UserImage(im.encoded_image, im.image_embeddings) for im in db_user.images],
            verification_model=db_user.verification_model
        )

    @classmethod
    def _to_db_user(cls, domain_user):
        return DbUser(
            username=domain_user.username,
            images=[DbUserImage(encoded_image=im.encoded_image, image_embeddings=im.image_embeddings)
                    for im in domain_user.images],
            verification_model=domain_user.verification_model
        )
