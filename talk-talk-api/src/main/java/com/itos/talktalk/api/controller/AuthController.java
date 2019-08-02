package com.itos.talktalk.api.controller;

import com.itos.talktalk.api.payload.request.AuthRequest;
import com.itos.talktalk.api.payload.response.AuthResponse;
import com.itos.talktalk.api.security.AccessTokenProvider;
import com.itos.talktalk.api.swagger.AuthControllerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDefinition {

    private AuthenticationManager authenticationManager;

    private AccessTokenProvider accessTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AccessTokenProvider accessTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> createAccessToken(
            @RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthResponse response = new AuthResponse();
        response.setToken(accessTokenProvider.generateAccessToken((UserDetails) authentication.getPrincipal()));
        return ResponseEntity.ok(response);
    }
}
