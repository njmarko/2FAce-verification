package com.xor.face.security.authentication.provider;

import com.xor.face.service.IUserService;
import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FaceVerificationAuthenticationProviderFactory {
    private final IUserService userService;
    private final IFaceVerification2FA faceVerificationProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FaceVerificationAuthenticationProviderFactory(IUserService userService, IFaceVerification2FA faceVerificationProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.faceVerificationProvider = faceVerificationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public FaceVerificationAuthenticationProvider faceVerificationAuthenticationProvider() {
        var provider = new FaceVerificationAuthenticationProvider(userService, faceVerificationProvider);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
