package com.xor.face.twofa.core.config;

import com.xor.face.twofa.core.client.IFaceVerificationClient;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.context.annotation.Bean;

public abstract class FaceVerificationClientFallbackConfigBase {
    protected final CircuitBreakerRegistry registry;
    protected final IFaceVerificationClient fallbackClient;
    protected final String serviceName;

    protected FaceVerificationClientFallbackConfigBase(CircuitBreakerRegistry registry, IFaceVerificationClient fallbackClient, String serviceName) {
        this.registry = registry;
        this.fallbackClient = fallbackClient;
        this.serviceName = serviceName;
    }

    @Bean
    public Feign.Builder feignBuilder() {
        var circuitBreaker = registry.circuitBreaker(serviceName);
        FeignDecorators decorators = FeignDecorators.builder()
                                                    .withCircuitBreaker(circuitBreaker)
                                                    .withFallback(fallbackClient)
                                                    .build();
        return Resilience4jFeign.builder(decorators);
    }
}
