package com.xor.face.twofa.core.verification;

import com.xor.face.twofa.core.RegistrationResult;
import com.xor.face.twofa.core.VerificationResult;
import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;

public interface IFaceVerification2FA {
    VerificationResult isValid(ILoginCredentials credentials);
    RegistrationResult register(IRegistrationCredentials credentials);
}
