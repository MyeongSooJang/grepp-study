package com.grepp.jms.greppstudy.member.presentation.util;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.JwtValidatorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.private-key}")
    private String privateKey;
    @Value("${jwt.public-key}")
    private String publicKey;

    private static final long JWT_EXPIRATION_MS = 1000 * 60 * 60;
    private static final long JWT_REFRESH_EXPIRATION_MS = 86400000L * 7;

    public String generateToken(Authentication authentication)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
        return Jwts.builder().subject((String) authentication.getPrincipal())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(loadPrivateKey())
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(loadPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();  // Jws<Claims>에서 Claims 추출
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (JwtValidatorException validatorException) {
            validatorException.printStackTrace();
            throw validatorException;
        } catch (ExpiredJwtException expiredJwtException) {
            expiredJwtException.printStackTrace();
            throw expiredJwtException;
        }
    }

    public KeyPair makeRsaKey() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        KeyPair pair = generator.generateKeyPair();
        log.info("private key : {}", Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
        log.info("public key : {}", Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
        return pair;
    }

    private PublicKey loadPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    private PrivateKey loadPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

}