package com.itos.talktalk.api.service.impl;

import com.itos.talktalk.api.enums.RoleName;
import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.exception.UserNotFoundException;
import com.itos.talktalk.api.mapper.UserMapper;
import com.itos.talktalk.api.model.Role;
import com.itos.talktalk.api.model.User;
import com.itos.talktalk.api.payload.request.CreateUserRequest;
import com.itos.talktalk.api.payload.response.UserDataResponse;
import com.itos.talktalk.api.repository.UserRepository;
import com.itos.talktalk.api.service.IRoleService;
import com.itos.talktalk.api.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    private IRoleService roleService;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            IRoleService roleService,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDataResponse createNewUser(CreateUserRequest createUserRequest) throws RoleNotFoundException {
        Role userRole = roleService.retrieveRoleByRoleName(RoleName.USER);
        Set<Role> roles = Collections.singleton(userRole);
        User user = userMapper.createUserFromCreateUserRequest(createUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.createUserDataResponseFromUser(user);
    }

    @Override
    public UserDataResponse retrieveUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("Cannot retrieve user with username: " + username));

        return userMapper.createUserDataResponseFromUser(user);
    }
}
