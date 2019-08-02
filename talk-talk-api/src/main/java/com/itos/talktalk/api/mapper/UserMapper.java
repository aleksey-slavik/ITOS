package com.itos.talktalk.api.mapper;

import com.itos.talktalk.api.model.User;
import com.itos.talktalk.api.payload.request.CreateUserRequest;
import com.itos.talktalk.api.payload.response.UserDataResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User createUserFromCreateUserRequest(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        return user;
    }

    public UserDataResponse createUserDataResponseFromUser(User user) {
        UserDataResponse response = new UserDataResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setId(user.getId());
        response.setEnabled(user.isEnabled());
        return response;
    }
}
