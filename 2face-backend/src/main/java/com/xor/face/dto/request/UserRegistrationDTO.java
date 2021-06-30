package com.xor.face.dto.request;

import com.xor.face.twofa.core.credentials.registration.IRegistrationCredentials;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
public class UserRegistrationDTO implements IRegistrationCredentials {

    @NotBlank(message = "Username can't be empty.")
    private String username;

    @NotBlank(message = "Email can't be empty.")
    @Email(message = "Email is not in valid format.")
    private String email;

    @NotBlank(message = "Password can't be empty.")
    private String password;

    private Collection<String> images;

}
