package com.lmello.titer.auth.socialproviders;

import com.lmello.titer.auth.enums.AuthProvider;

public interface SocialTokenVerifier {

    AuthProvider provider();

    SocialIdentity verify(String token);
}
