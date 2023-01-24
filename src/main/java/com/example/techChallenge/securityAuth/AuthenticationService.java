package com.example.techChallenge.securityAuth;

import com.example.techChallenge.securityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getCustomerId());

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
