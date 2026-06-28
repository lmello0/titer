package com.lmello.titer.users.exceptions;

import com.lmello.titer.shared.exception.DomainException;
import com.lmello.titer.users.enums.Role;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(Role role) {
        super(role + " not found");
    }
}
