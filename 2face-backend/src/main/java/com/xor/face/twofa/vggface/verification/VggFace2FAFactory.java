package com.xor.face.twofa.vggface.verification;

import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import com.xor.face.twofa.core.verification.IFaceVerification2FAFactory;
import com.xor.face.twofa.vggface.client.IVggFaceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "vgg-face-factory")
public class VggFace2FAFactory implements IFaceVerification2FAFactory {
    private final IVggFaceClient vggFaceClient;

    @Autowired
    public VggFace2FAFactory(@Qualifier("vgg-face-client") IVggFaceClient vggFaceClient) {
        this.vggFaceClient = vggFaceClient;
    }

    @Override
    public IFaceVerification2FA create2FA() {
        return new VggFace2FA(vggFaceClient);
    }
}
