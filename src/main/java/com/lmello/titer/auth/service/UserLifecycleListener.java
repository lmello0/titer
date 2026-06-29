package com.lmello.titer.auth.service;

import com.lmello.titer.auth.repositories.UserAuthRepository;
import com.lmello.titer.users.api.events.UserDeactivatedEvent;
import com.lmello.titer.users.api.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLifecycleListener {

    private final UserAuthRepository userAuthRepository;
    private final RefreshTokenService refreshTokenService;

    @Async
    @EventListener
    public void onUserDeleted(UserDeletedEvent event) {
        userAuthRepository.deleteByUserId(event.userId());
        refreshTokenService.deleteAllForUser(event.userId());

        log.info("Cleared credentials and refresh tokens for deleted userId={}", event.userId());
    }

    @Async
    @EventListener
    public void onUserDeactivated(UserDeactivatedEvent event) {
        refreshTokenService.revokeAllForUser(event.userId());

        log.info("Revoked sessions for deactivated userId={}", event.userId());
    }
}
