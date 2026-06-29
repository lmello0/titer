package com.lmello.titer.auth.socialproviders;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.lmello.titer.auth.enums.AuthProvider;
import com.lmello.titer.auth.exceptions.InvalidSocialTokenException;
import com.lmello.titer.auth.properties.GoogleAuthProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class GoogleTokenVerifier implements SocialTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier(GoogleAuthProperties properties) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(properties.clientIds())
                .build();
    }

    @Override
    public AuthProvider provider() {
        return AuthProvider.GOOGLE;
    }

    @Override
    public SocialIdentity verify(String token) {
        GoogleIdToken idToken;

        try {
            idToken = verifier.verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidSocialTokenException("Could not verify Google token", e);
        }

        if (idToken == null) {
            throw new InvalidSocialTokenException("Google token is invalid or expired");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        return new SocialIdentity(
                payload.getSubject(),
                payload.getEmail(),
                Boolean.TRUE.equals(payload.getEmailVerified()),
                (String) payload.get("given_name"),
                (String) payload.get("family_name")
        );
    }
}
