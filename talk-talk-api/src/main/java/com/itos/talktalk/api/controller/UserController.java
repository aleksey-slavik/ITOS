package com.itos.talktalk.api.controller;

import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.exception.UserNotFoundException;
import com.itos.talktalk.api.payload.request.CreateUserRequest;
import com.itos.talktalk.api.payload.response.UserDataResponse;
import com.itos.talktalk.api.service.IUserService;
import com.itos.talktalk.api.swagger.UserControllerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDefinition {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(
            @PathVariable(value = "username") String username) throws UserNotFoundException {

        UserDataResponse response = userService.retrieveUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest)
            throws RoleNotFoundException {

        UserDataResponse response = userService.createNewUser(createUserRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/v1/users/{username}")
                .buildAndExpand(response.getUsername()).toUri();

        return ResponseEntity.created(location).body(response);
    }
}
