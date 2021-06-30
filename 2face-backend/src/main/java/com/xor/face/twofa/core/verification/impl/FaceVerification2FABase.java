package com.xor.face.twofa.core.verification.impl;

import com.xor.face.twofa.core.RegistrationResult;
import com.xor.face.twofa.core.VerificationResult;
import com.xor.face.twofa.core.client.IFaceVerificationClient;
import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;
import com.xor.face.twofa.core.verification.IFaceVerification2FA;

public abstract class FaceVerification2FABase implements IFaceVerification2FA {
    protected abstract IFaceVerificationClient client();

    @Override
    public VerificationResult verify(ILoginCredentials credentials) {
        return client().verify(credentials);
    }

    @Override
    public RegistrationResult register(IRegistrationCredentials credentials) {
        return client().register(credentials);
    }
}
