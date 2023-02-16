package com.example.techChallenge.securityAuth;

import com.example.techChallenge.securityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    /**
     * EXPLAIN: Since we adopt Lombok here, we don't need to implement constructor for class.
     */
    private final JwtService jwtService;
    /**
     * EXPLAIN: Rather than annotate @Autowire before the service, we use the final keyword
     * to make the field immutable and use constructor injection, as it's more
     * stable and maintainable.
     */
    private final JwtUserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getCustomerId());

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
