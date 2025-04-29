package hongik.Todoing.domain.jwt;

import hongik.Todoing.domain.login.PrincipalDetails;
import hongik.Todoing.global.util.RedisUtil;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.GrantedAuthority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {


    private final SecretKey secretKey;
    private final Long access;
    private final Long refreshTokenExpiration;
    private final RedisUtil redisUtil;

    public JwtUtil(@Value("${spring.jwt.secret") String secret,
                   @Value("${spring.jwt.token.access-token-expire-time}") Long access,
                   @Value("${spring.jwt.token.refresh-token-expire-time") Long refreshTokenExpiration,
                   RedisUtil redisUtil) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.access = access;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisUtil = redisUtil;
    }

    // JWT 토큰 입력으로 받아 토큰의 페이로드에서 사용자 이름 추출
    public String getUsername(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRoles(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", String.class);
    }

    public long getExpirationTime(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime();
    }

    public String tokenProvider(PrincipalDetails principalDetails, Instant expiration) {
        Instant issuedAt = Instant.now();
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(principalDetails.getUsername())
                .claim("roles", authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtAccessToken(PrincipalDetails principalDetails) {
        Instant expiration = Instant.now().plusMillis(access);
        return tokenProvider(principalDetails, expiration);
    }

    public String createJwtRefreshToken(PrincipalDetails principalDetails) {
        Instant expiration = Instant.now().plusMillis(refreshTokenExpiration);
        String refreshToken = tokenProvider(principalDetails, expiration);
        redisUtil.save(
                principalDetails.getUsername(),
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }




}
