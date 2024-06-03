package com.khanh.antimessenger.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.khanh.antimessenger.constant.SecurityConstant;
import com.khanh.antimessenger.model.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.khanh.antimessenger.constant.SecurityConstant.ISSUER;
import static com.khanh.antimessenger.constant.SecurityConstant.TOKEN_CANNOT_BE_VERIFIED;

@Component
public class RefreshTokenService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserPrincipal userPrincipal, HttpServletRequest request) {
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuer(SecurityConstant.ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.REFRESH_TOKEN_EXPIRATION_TIME))
                .withClaim("createdByIp", request.getRemoteAddr())
                .withClaim("createdByHost", request.getRemoteHost())
                .sign(getAlgorithm());
    }

    public String getSubject(String token) {
        return getDecodedJwt(token).getSubject();
    }

    public String getCreatedByIp(String token) {
        return getDecodedJwt(token).getClaim("createdByIp").asString();
    }

    public String getCreatedByHost(String token) {
        return getDecodedJwt(token).getClaim("createdByHost").asString();
    }

    public boolean isTokenValid(String username, String token) {
        return StringUtils.isNotEmpty(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getDecodedJwt(token).getExpiresAt().before(new Date());
    }

    private DecodedJWT getDecodedJwt(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaimPresence("createdByIp")
                    .withClaimPresence("createdByHost")
                    .build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return decodedJWT;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret.getBytes());
    }
}
