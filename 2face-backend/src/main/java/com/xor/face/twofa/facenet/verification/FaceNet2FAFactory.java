package com.xor.face.twofa.facenet.verification;

import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import com.xor.face.twofa.core.verification.IFaceVerification2FAFactory;
import com.xor.face.twofa.facenet.client.IFaceNetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "face-net-factory")
public class FaceNet2FAFactory implements IFaceVerification2FAFactory {
    private final IFaceNetClient faceNetClient;

    @Autowired
    public FaceNet2FAFactory(@Qualifier("face-net-client") IFaceNetClient faceNetClient) {
        this.faceNetClient = faceNetClient;
    }

    @Override
    public IFaceVerification2FA create2FA() {
        return new FaceNet2FA(faceNetClient);
    }
}
