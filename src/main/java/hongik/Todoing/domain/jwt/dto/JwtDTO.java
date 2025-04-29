package hongik.Todoing.domain.jwt.dto;

public record JwtDTO (
        String accessToken,
        String refreshToken
) {
}
