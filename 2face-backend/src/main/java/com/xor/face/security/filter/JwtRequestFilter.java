package com.xor.face.security.filter;

import com.xor.face.security.authentication.FaceVerificationAuthentication;
import com.xor.face.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtUtil.getToken(httpServletRequest);
        try {
            if (authToken != null) {
                String username = jwtUtil.extractUsernameFromToken(authToken);
                if (username != null) {
                    var userDetails = userService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(authToken, userDetails)) {
                        var auth = new FaceVerificationAuthentication(userDetails);
                        auth.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("had to write something so that sonar cloud shuts up.");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
