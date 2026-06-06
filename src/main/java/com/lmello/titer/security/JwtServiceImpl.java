package com.lmello.titer.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public String generateToken(
            UUID userId,
            String username,
            String email,
            Set<String> authorities
    ) {
        Instant now = Instant.now();

        String scope = String.join(" ", authorities);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtProperties.expirationSeconds()))
                .subject(userId.toString())
                .claim("username", username)
                .claim("email", email)
                .claim("scope", scope)
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}
