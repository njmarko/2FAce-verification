package com.xor.face.security.authentication;

import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class FaceVerificationAuthenticationToken extends UsernamePasswordAuthenticationToken implements ILoginCredentials {
    private final String encodedImage;

    public FaceVerificationAuthenticationToken(String username, String password, String encodedImage) {
        super(username, password);
        this.encodedImage = encodedImage;
    }

    @Override
    public String getUsername() {
        return (String) getPrincipal();
    }

    @Override
    public String getImage() {
        return encodedImage;
    }
}
