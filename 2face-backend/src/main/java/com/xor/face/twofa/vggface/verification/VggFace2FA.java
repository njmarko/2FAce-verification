package com.xor.face.twofa.vggface.verification;


import com.xor.face.twofa.core.client.IFaceVerificationClient;
import com.xor.face.twofa.core.verification.impl.FaceVerification2FABase;
import com.xor.face.twofa.vggface.client.IVggFaceClient;

public class VggFace2FA extends FaceVerification2FABase {
    private final IVggFaceClient vggFaceClient;

    public VggFace2FA(IVggFaceClient vggFaceClient) {
        this.vggFaceClient = vggFaceClient;
    }

    @Override
    protected IFaceVerificationClient client() {
        return vggFaceClient;
    }
}
