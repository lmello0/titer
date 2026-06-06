package com.lmello.titer.users.internal.exception;

import com.lmello.titer.shared.exception.DomainException;
import com.lmello.titer.users.internal.enums.Role;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(Role role) {
        super(role + " not found");
    }
}
