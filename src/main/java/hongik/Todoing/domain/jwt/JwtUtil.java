package hongik.Todoing.domain.jwt;

import hongik.Todoing.domain.auth.PrincipalDetails;
import hongik.Todoing.domain.auth.service.PrincipalDetailService;
import hongik.Todoing.domain.jwt.dto.JwtDTO;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import hongik.Todoing.global.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final PrincipalDetailService principalDetailService;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${spring.jwt.token.access-token-expire-time}") Long access,
                   @Value("${spring.jwt.token.refresh-token-expire-time}") Long refreshTokenExpiration,
                   RedisUtil redisUtil, PrincipalDetailService principalDetailService) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.access = access;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisUtil = redisUtil;
        this.principalDetailService = principalDetailService;
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

        // redisUtil이 null이 아니고, Redis 연결이 되어있을 때만 저장
        try {
            if (redisUtil != null) {
                redisUtil.save(
                        principalDetails.getUsername(),
                        refreshToken,
                        refreshTokenExpiration,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (Exception e) {
            log.warn("[*] Redis 저장 실패: 로컬 환경이거나 Redis 서버 없음");
        }

        return refreshToken;
    }

    // HTTP 요ㅓㅇ 시 Authorization header에서 JWT token 검색
    public String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("[*] JWT Token not found");
            return null;
        }
        log.info("[*] JWT Token found");

        return authorizationHeader.split(" ")[1];
    }

    // token validation test
    public void validationToken(String token) {
        try {
            // Jwt 만료 시간 검증 시 클라이언트와 서버 시간 차이 고려
            long second = 3 * 60;

            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(second)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());

            log.info("Authorization with Token");

            if(isExpired) {
                log.info("[*] Token is Expired");
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Token is Invalid");
        } catch (ExpiredJwtException e) {
            log.info("Token is Expired");
        } catch (UnsupportedJwtException e) {
            log.info("Token is Unsupported");
        } catch (IllegalArgumentException e) {
            log.info("Token is Illegal");
        }
    }

    public boolean validateRefreshToken(String refreshToken) throws SignatureException {
        String username = getUsername(refreshToken);

        if(!redisUtil.hasKey(username)) {
            throw new GeneralException(ErrorStatus.INVALID_PARAMETER);
        }
        return true;
    }

    public JwtDTO reissueToken(String refreshToken) throws SignatureException {
        UserDetails userDetails = principalDetailService.loadUserByUsername(getUsername(refreshToken));

        return new JwtDTO(
                createJwtAccessToken((PrincipalDetails) userDetails),
                createJwtRefreshToken((PrincipalDetails) userDetails)
        );
    }

    public String createAccessToken(String email, String role) {
        Instant expiration = Instant.now().plusMillis(access);

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .subject(email)
                .claim("roles", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

}
