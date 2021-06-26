package com.xor.face.twofa.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class VerificationResult {
    private boolean verificationSuccessfull;

    public static VerificationResult failure() {
        return new VerificationResult(false);
    }
}
