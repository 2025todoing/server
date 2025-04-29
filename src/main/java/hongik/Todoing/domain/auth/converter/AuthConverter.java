package hongik.Todoing.domain.auth.converter;

import hongik.Todoing.domain.auth.dto.KakaoLoginResponseDto;
import hongik.Todoing.domain.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthConverter {

    public static Member toMember(String email, String name, String password, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .role("ROLE_USER")
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }

    public static KakaoLoginResponseDto JoinResponse(Member member, String accessToken) {
        return KakaoLoginResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .accessToken(accessToken)
                .build();

    }
}
