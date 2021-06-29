package com.xor.face.twofa.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VerificationResult {
    private boolean verificationSuccessful;

    public static VerificationResult failure() {
        return new VerificationResult(false);
    }

    public boolean isFailure() {
        return !verificationSuccessful;
    }
}
