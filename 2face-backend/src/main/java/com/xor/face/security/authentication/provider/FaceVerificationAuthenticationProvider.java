package com.xor.face.security.authentication.provider;

import com.xor.face.domain.entities.User;
import com.xor.face.security.authentication.FaceVerificationAuthenticationToken;
import com.xor.face.service.IUserService;
import com.xor.face.twofa.core.verification.IFaceVerification2FA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class FaceVerificationAuthenticationProvider extends DaoAuthenticationProvider {
    private final IUserService userService;
    private final IFaceVerification2FA faceVerificationProvider;

    public FaceVerificationAuthenticationProvider(IUserService userService, IFaceVerification2FA faceVerificationProvider) {
        this.userService = userService;
        this.faceVerificationProvider = faceVerificationProvider;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        var authentication = (FaceVerificationAuthenticationToken) auth;
        var user = getUser(authentication);
        Authentication result = super.authenticate(auth);
        var verificationResult = faceVerificationProvider.isValid(authentication);
        if (verificationResult.isFailure()) {
            throw new BadCredentialsException("Face verification 2FA was unsuccessful.");
            // TODO: Offer additional 2FA mechanism such as TOTP
        }
        return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(FaceVerificationAuthenticationToken.class);
    }

    private User getUser(FaceVerificationAuthenticationToken auth) {
        var user = userService.findByUsernameWithAuthorities(auth.getName());
        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return user;
    }
}
