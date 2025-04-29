package hongik.Todoing.domain.auth.service;

import hongik.Todoing.domain.auth.converter.AuthConverter;
import hongik.Todoing.domain.jwt.JwtUtil;
import hongik.Todoing.domain.kakao.KakaoUtil;
import hongik.Todoing.domain.kakao.dto.KakaoDTO;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.generate.AccessControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public Member oAuthLogin(String accessCode, HttpServletResponse response) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile= kakaoUtil.requestProfile(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> createNewMember(kakaoProfile));

        String token = jwtUtil.createAccessToken(member.getEmail(), member.getRole().toString());
        response.setHeader("Authorization", token);

        return member;
    }

    private Member createNewMember(KakaoDTO.KakaoProfile kakaoProfile) {
        Member newMember = AuthConverter.toMember(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getProperties().getNickname(),
                null,
                passwordEncoder
        );

        return memberRepository.save(newMember);
    }
}
