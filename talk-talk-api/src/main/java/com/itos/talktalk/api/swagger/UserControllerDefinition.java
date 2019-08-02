package com.itos.talktalk.api.swagger;

import com.itos.talktalk.api.exception.RoleNotFoundException;
import com.itos.talktalk.api.exception.UserNotFoundException;
import com.itos.talktalk.api.payload.request.CreateUserRequest;
import com.itos.talktalk.api.payload.response.UserDataResponse;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

@Api("Operations with users")
public interface UserControllerDefinition {

    /**
     * This API retrieve user data by given username
     *
     * @param username given username
     * @return user data
     * @throws UserNotFoundException throws in case if user with given username not found in database
     */
    @ApiOperation(
            value = "Retrieve user data by username",
            httpMethod = "GET",
            nickname = "retrieveUserByUsername",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            {
                    @ApiResponse(
                            code = HttpServletResponse.SC_OK,
                            message = "operation successfully completed",
                            response = UserDataResponse.class
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_BAD_REQUEST,
                            message = "errors during validation"
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            message = "internal server error"
                    )
            })
    ResponseEntity<?> getUserByUsername(
            @ApiParam(value = "The user username", required = true) String username) throws UserNotFoundException;

    /**
     * This API create new user with USER role
     *
     * @param createUserRequest user data
     * @return {@link ResponseEntity} with created user data
     * @throws RoleNotFoundException throws in case if USER role not exist in database
     */
    @ApiOperation(
            value = "Create new user",
            httpMethod = "POST",
            nickname = "createNewUser",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(
            @ApiImplicitParam(
                    paramType = "body",
                    name = "createUserRequest",
                    required = true,
                    dataType = "CreateUserRequest",
                    dataTypeClass = CreateUserRequest.class
            )
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            code = HttpServletResponse.SC_OK,
                            message = "operation successfully completed",
                            response = UserDataResponse.class
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_BAD_REQUEST,
                            message = "errors during creation"
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            message = "internal server error"
                    )
            })
    ResponseEntity<?> createUser(CreateUserRequest createUserRequest) throws RoleNotFoundException;
}
