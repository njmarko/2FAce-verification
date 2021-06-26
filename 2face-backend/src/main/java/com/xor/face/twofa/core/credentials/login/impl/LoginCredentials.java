package com.xor.face.twofa.core.credentials.login.impl;

import com.xor.face.twofa.core.credentials.login.ILoginCredentials;

public class LoginCredentials implements ILoginCredentials {
    private final String username;
    private final String image;

    public LoginCredentials(String username, String image) {
        this.username = username;
        this.image = image;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getImage() {
        return image;
    }
}
