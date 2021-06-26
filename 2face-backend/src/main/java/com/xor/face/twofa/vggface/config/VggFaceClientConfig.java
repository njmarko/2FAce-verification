package com.xor.face.twofa.vggface.config;

import com.xor.face.twofa.core.config.FaceVerificationClientFallbackConfigBase;
import com.xor.face.twofa.vggface.client.fallback.VggFaceClientFallback;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VggFaceClientConfig extends FaceVerificationClientFallbackConfigBase {
    public VggFaceClientConfig(CircuitBreakerRegistry registry, VggFaceClientFallback fallbackClient) {
        super(registry, fallbackClient, "vgg-face-verification");
    }
}
