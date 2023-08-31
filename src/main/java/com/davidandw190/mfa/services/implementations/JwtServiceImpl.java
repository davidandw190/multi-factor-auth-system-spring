package com.davidandw190.mfa.services.implementations;

import com.davidandw190.mfa.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT (JSON Web Token) related operations.
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static String SECRET_KEY;
    private static long TOKEN_EXPIRATION;
    private static long REFRESH_TOKEN_EXPIRATION;

    @Autowired
    public JwtServiceImpl(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.expiration}") long tokenExpiration,
            @Value("${application.security.jwt.refresh-token.expiration}") long refreshExpiration) {

        SECRET_KEY = secretKey;
        TOKEN_EXPIRATION = tokenExpiration;
        REFRESH_TOKEN_EXPIRATION = refreshExpiration;

    }


    /**
     * Generates a JWT token with additional claims and user details.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails User details for the token.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return buildToken(extraClaims, userDetails, TOKEN_EXPIRATION);
    }


    /**
     * Generates a JWT token with user details and no additional claims.
     *
     * @param userDetails User details for the token.
     * @return The generated JWT token.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    /**
     * Generates a refresh JWT token with user details and no additional claims.
     *
     * @param userDetails User details for the token.
     * @return The generated refresh JWT token.
     */
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION);
    }


    /**
     * Extracts a specific claim from the provided JWT token.
     *
     * @param token          JWT token from which to extract the claim.
     * @param claimsResolver Function that resolves the desired claim from the token's claims.
     * @param <T>            Type of the claim value.
     * @return The extracted claim value.
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Validates whether a JWT token is valid for the given user details.
     *
     * @param token       JWT token to be validated.
     * @param userDetails UserDetails of the user against which to validate the token.
     * @return True if the token is valid for the user, otherwise false.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token from which to extract the username.
     * @return Extracted username.
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration)  {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
