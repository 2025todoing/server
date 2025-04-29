package hongik.Todoing.domain.auth.dto;

import lombok.Builder;

@Builder
public class KakaoLoginResponseDto {
    private String email;
    private String name;
    private String accessToken;
}
