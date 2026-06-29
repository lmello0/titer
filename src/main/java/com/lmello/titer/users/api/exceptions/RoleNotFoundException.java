package com.lmello.titer.users.api.exceptions;

import com.lmello.titer.shared.exception.DomainException;
import com.lmello.titer.users.enums.Role;

public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }
}
