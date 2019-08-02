package com.itos.talktalk.api.exception;

import com.itos.talktalk.api.enums.RoleName;

public class RoleNotFoundException extends Exception {

    public RoleNotFoundException(RoleName roleName) {
        super("Cannot retrieve role: " + roleName);
    }
}
