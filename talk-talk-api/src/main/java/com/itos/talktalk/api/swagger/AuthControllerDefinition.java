package com.itos.talktalk.api.swagger;

import com.itos.talktalk.api.payload.request.AuthRequest;
import com.itos.talktalk.api.payload.response.AuthResponse;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

@Api("Authentication operations")
public interface AuthControllerDefinition {

    /**
     * This API check given credentials and generate access token.
     *
     * @param authRequest user credentials
     * @return {@link ResponseEntity} with generated access token
     */
    @ApiOperation(
            value = "Generate authentication token",
            httpMethod = "POST",
            nickname = "createAccessToken",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)

    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            paramType = "body",
                            name = "authRequest",
                            required = true,
                            dataType = "AuthRequest",
                            dataTypeClass = AuthRequest.class
                    )
            })

    @ApiResponses(
            {
                    @ApiResponse(
                            code = HttpServletResponse.SC_OK,
                            message = "operation successfully completed",
                            response = AuthResponse.class
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_BAD_REQUEST,
                            message = "validation failed"
                    ),
                    @ApiResponse(
                            code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            message = "internal server error"
                    )
            })
    ResponseEntity<?> createAccessToken(AuthRequest authRequest);
}
