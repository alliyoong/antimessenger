package com.khanh.antimessenger.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.khanh.antimessenger.constant.TokenType;
import com.khanh.antimessenger.model.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.khanh.antimessenger.constant.SecurityConstant.*;

@Component
@Slf4j
public class AccessTokenService {
//    @Value("${jwt.parent-dir}")
//    private String parentDirName;
    @Value("${jwt.public-key}")
    private String publicKeyFileName;
    @Value("${jwt.private-key}")
    private String privateKeyFileName;

    public String generateToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withAudience(AUDIENCE)
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .sign(getAlgorithm());
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    private String[] getClaimsFromToken(String token) {
        return getDecodedJwt(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        return Arrays.stream(getClaimsFromToken(token)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getSubject(String token) {
        return getDecodedJwt(token).getSubject();
    }

    public boolean isTokenValid(String username, String token) {
        return StringUtils.isNotEmpty(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getDecodedJwt(token).getExpiresAt().before(new Date());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private DecodedJWT getDecodedJwt(String token) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return decodedJWT;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.RSA512(getPublicKey(), getPrivateKey());
    }

    private RSAPublicKey getPublicKey() {
        PublicKey pk;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            var classLoader = getClass().getClassLoader();
            var publicKeyFile = new File(classLoader.getResource(publicKeyFileName).getFile());
            log.info( String.format("Path to public key file: %s", publicKeyFile.toPath()));
//            byte[] encodedKey = Files.readAllBytes(Paths.get("", publicKeyFile).toAbsolutePath());
            byte[] encodedKey = Files.readAllBytes(publicKeyFile.toPath());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
            pk = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(PUBLIC_KEY_FILE_CANNOT_BE_FOUND);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return (RSAPublicKey) pk;
    }

    private RSAPrivateKey getPrivateKey() {
        PrivateKey pk;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            ClassLoader classLoader = getClass().getClassLoader();
            var privateKeyFile = new File(classLoader.getResource(privateKeyFileName).getFile());
            log.info( String.format("Path to private key file: %s", privateKeyFile.toPath()));
//            byte[] encodedKey = Files.readAllBytes(Paths.get("", privateKeyFile).toAbsolutePath());
            byte[] encodedKey = Files.readAllBytes(privateKeyFile.toPath());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            pk = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(PRIVATE_KEY_FILE_CANNOT_BE_FOUND);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return (RSAPrivateKey) pk;
    }
}
