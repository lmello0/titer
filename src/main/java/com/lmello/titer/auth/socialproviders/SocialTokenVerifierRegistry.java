package com.lmello.titer.auth.socialproviders;

import com.lmello.titer.auth.enums.AuthProvider;
import com.lmello.titer.auth.exceptions.UnsupportedProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SocialTokenVerifierRegistry {

    private final Map<AuthProvider, SocialTokenVerifier> verifiers;

    public SocialTokenVerifierRegistry(List<SocialTokenVerifier> verifiers) {
        this.verifiers = verifiers.stream()
                .collect(Collectors.toUnmodifiableMap(SocialTokenVerifier::provider, Function.identity()));

        log.info(
                "Social token verifier registry initialized with {} verifiers: {}",
                verifiers.size(),
                verifiers.stream()
                        .map(v -> v.getClass().getSimpleName())
                        .toList()
        );
    }

    public SocialTokenVerifier get(AuthProvider provider) {
        SocialTokenVerifier verifier = verifiers.get(provider);

        if (verifier == null) {
            throw new UnsupportedProviderException(provider.name());
        }

        return verifier;
    }
}
