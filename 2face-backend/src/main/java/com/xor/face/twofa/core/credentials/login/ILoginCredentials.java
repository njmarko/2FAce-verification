package com.xor.face.twofa.core.credentials.login;

import java.io.Serializable;

public interface ILoginCredentials extends Serializable {
    String getUsername();
    String getImage();
}
