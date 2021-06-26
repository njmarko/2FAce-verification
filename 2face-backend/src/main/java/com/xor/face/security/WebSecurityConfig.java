package com.xor.face.security;


import com.xor.face.security.filter.JwtRequestFilter;
import com.xor.face.security.authentication.provider.FaceVerificationAuthenticationProviderFactory;
import com.xor.face.security.util.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final FaceVerificationAuthenticationProviderFactory faceVerificationAuthenticationProviderFactory;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(FaceVerificationAuthenticationProviderFactory faceVerificationAuthenticationProviderFactory, RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                             JwtRequestFilter jwtRequestFilter) {
        this.faceVerificationAuthenticationProviderFactory = faceVerificationAuthenticationProviderFactory;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        var provider = faceVerificationAuthenticationProviderFactory.faceVerificationAuthenticationProvider();
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/users/authenticate").permitAll()
                .antMatchers("/api/users/2fa").permitAll()
                .antMatchers("/api/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
