from domain.entities import User
from domain.exceptions import UsernameNotFoundException


class UserRepository(object):

    def find_one(self, username):
        user = User.objects(username=username).first()
        if not user:
            raise UsernameNotFoundException(f"Cannot find user with username {username}.")
        return user

    def save(self, user):
        user.save()
        return user
