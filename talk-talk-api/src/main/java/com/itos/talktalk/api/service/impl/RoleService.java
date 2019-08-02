package com.itos.talktalk.api.service.impl;

import com.itos.talktalk.api.enums.RoleName;
import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.model.Role;
import com.itos.talktalk.api.repository.RoleRepository;
import com.itos.talktalk.api.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role retrieveRoleByRoleName(RoleName roleName) throws RoleNotFoundException {
        return roleRepository.findByRole(roleName.name())
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }
}
