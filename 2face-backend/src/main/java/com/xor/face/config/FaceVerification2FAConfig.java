package com.xor.face.config;

import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import com.xor.face.twofa.core.verification.IFaceVerification2FAFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FaceVerification2FAConfig {
    private final IFaceVerification2FAFactory faceVerification2FAFactory;

    @Autowired
    public FaceVerification2FAConfig(@Qualifier("face-net-factory") IFaceVerification2FAFactory faceVerification2FAFactory) {
        this.faceVerification2FAFactory = faceVerification2FAFactory;
    }

    @Bean
    public IFaceVerification2FA getFaceVerification2FABean() {
        return faceVerification2FAFactory.create2FA();
    }
}
