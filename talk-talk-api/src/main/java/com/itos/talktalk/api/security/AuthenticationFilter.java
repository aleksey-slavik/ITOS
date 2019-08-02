package com.itos.talktalk.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {

    private AccessTokenProvider accessTokenProvider;

    private UserPrincipalService userPrincipalService;

    @Autowired
    public AuthenticationFilter(AccessTokenProvider accessTokenProvider, UserPrincipalService userPrincipalService) {
        this.accessTokenProvider = accessTokenProvider;
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws
            ServletException,
            IOException {

        String token = extractTokenFromRequest(request);

        if (token != null ) {
            String username = accessTokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = userPrincipalService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("X-Auth-Token ")) {
            return token.substring(13);
        } else {
            return null;
        }
    }
}
