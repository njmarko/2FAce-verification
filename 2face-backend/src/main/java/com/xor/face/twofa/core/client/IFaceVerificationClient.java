package com.xor.face.twofa.core.client;

import com.xor.face.twofa.core.RegistrationResult;
import com.xor.face.twofa.core.VerificationResult;
import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;

public interface IFaceVerificationClient {
    VerificationResult verify(ILoginCredentials userCredentials);
    RegistrationResult register(IRegistrationCredentials userCredentials);
}
