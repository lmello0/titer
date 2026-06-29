package com.lmello.titer.users.api.events;

import com.lmello.titer.users.api.representation.UserInfo;

public record UserCreatedEvent(UserInfo userCreatedData) {
}
