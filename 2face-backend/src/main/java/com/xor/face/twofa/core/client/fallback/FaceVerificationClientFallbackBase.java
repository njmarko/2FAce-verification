package com.xor.face.twofa.core.client.fallback;

import com.xor.face.twofa.core.RegistrationResult;
import com.xor.face.twofa.core.VerificationResult;
import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;
import com.xor.face.twofa.facenet.client.IFaceNetClient;

public abstract class FaceVerificationClientFallbackBase implements IFaceNetClient {
    @Override
    public VerificationResult verify(ILoginCredentials userCredentials) {
        return VerificationResult.failure();
    }

    @Override
    public RegistrationResult register(IRegistrationCredentials userCredentials) {
        return RegistrationResult.failure();
    }
}
