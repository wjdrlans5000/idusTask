package com.example.idustask.auth.config;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
public class JwtTokenUtil {

    private String secret;

    private long jwt_token_validity;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //토큰으로 유저네임 확인
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            LOGGER.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            LOGGER.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            LOGGER.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            LOGGER.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            LOGGER.info("JWT token compact of handler are invalid.");
        }
        return null;
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(String id) {
        return generateToken(id, new HashMap<>());
    }

    public String generateToken(String id, Map<String, Object> claims) {
        return doGenerateToken(id, claims);
    }

    private String doGenerateToken(String id, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwt_token_validity * 100)) //토큰 만료시간 설정 1시간
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token) {
        return getAllClaimsFromToken(token) != null;
    }
}
