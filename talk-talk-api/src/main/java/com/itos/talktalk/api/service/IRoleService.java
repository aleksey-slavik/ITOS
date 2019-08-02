package com.itos.talktalk.api.service;

import com.itos.talktalk.api.enums.RoleName;
import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.model.Role;

public interface IRoleService {

    Role retrieveRoleByRoleName(RoleName roleName) throws RoleNotFoundException;
}
