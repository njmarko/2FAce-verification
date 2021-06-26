package com.xor.face.twofa.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegistrationResult {
    private boolean registrationSuccessfull;

    public static RegistrationResult failure() {
        return new RegistrationResult(false);
    }
}
