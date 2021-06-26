package com.xor.face.twofa.facenet.client;

import com.xor.face.twofa.core.RegistrationResult;
import com.xor.face.twofa.core.VerificationResult;
import com.xor.face.twofa.core.client.IFaceVerificationClient;
import com.xor.face.twofa.core.credentials.login.ILoginCredentials;
import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;
import com.xor.face.twofa.facenet.config.FaceNetClientConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "face-net-verification")
@FeignClient(name = "face-net-verification", qualifiers = "face-net-client", url = "http://localhost:8181", configuration = FaceNetClientConfig.class)
public interface IFaceNetClient extends IFaceVerificationClient {
    @PostMapping(value = "/facenet")
    VerificationResult verify(@RequestBody ILoginCredentials userCredentials);

    @PostMapping(value = "/register/facenet")
    RegistrationResult register(@RequestBody IRegistrationCredentials userCredentials);
}
