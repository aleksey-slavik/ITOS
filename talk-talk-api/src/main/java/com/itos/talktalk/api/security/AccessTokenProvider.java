package com.itos.talktalk.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class AccessTokenProvider {

    @Value("${jwt.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${jwt.accessTokenValiditySeconds}")
    private int accessTokenValiditySeconds;

    /**
     * Generate access token for user
     *
     * @param userDetails user details
     * @return generated access token
     */
    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValiditySeconds))
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }

    /**
     * Validate token according to user details
     *
     * @param token given token
     * @param userDetails user details
     * @return true, if token is valid, false in otherwise
     */
    public boolean validateAccessToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Retrieve username from JWT token
     *
     * @param token given token
     * @return username retrieved from given token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve expiration date from token
     *
     * @param token given token
     * @return expiration date retrieved from token
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieve value of specified parameter from token
     *
     * @param token given token
     * @param claimsResolver function for resolve value
     * @param <T> type of needed parameter
     * @return value of needed parameter
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieve all data from token using secret key
     *
     * @param token given token
     * @return data from token collected in {@link Claims} object
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(accessTokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token expired
     *
     * @param token given token
     * @return true, if token expired, false in otherwise
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
}
