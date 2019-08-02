package com.itos.talktalk.api.service;

import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.exception.UserNotFoundException;
import com.itos.talktalk.api.payload.request.CreateUserRequest;
import com.itos.talktalk.api.payload.response.UserDataResponse;

public interface IUserService {

    UserDataResponse createNewUser(CreateUserRequest createUserRequest) throws RoleNotFoundException;

    UserDataResponse retrieveUserByUsername(String username) throws UserNotFoundException;
}
