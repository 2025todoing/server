package hongik.Todoing.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoLoginResponseDto {
    private String email;
    private String name;
    private String accessToken;
}
