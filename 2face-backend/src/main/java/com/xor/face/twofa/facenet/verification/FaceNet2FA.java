package com.xor.face.twofa.facenet.verification;

import com.xor.face.twofa.core.client.IFaceVerificationClient;
import com.xor.face.twofa.core.verification.impl.FaceVerification2FABase;
import com.xor.face.twofa.facenet.client.IFaceNetClient;

public class FaceNet2FA extends FaceVerification2FABase {
    private final IFaceNetClient faceNetClient;

    public FaceNet2FA(IFaceNetClient faceNetClient) {
        this.faceNetClient = faceNetClient;
    }

    @Override
    protected IFaceVerificationClient client() {
        return faceNetClient;
    }
}
