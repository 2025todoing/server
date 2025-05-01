package hongik.Todoing.global.dummy;

import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.attribute.FileAttribute;

@Configuration
@RequiredArgsConstructor
public class DummyDataConfig {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDummyMember() {
        return args -> {
            if (memberRepository.findByName("todoingTest").isEmpty()) { // 혹시 이미 있을까봐 체크
                Member dummy = Member.builder()
                        .name("todoingTest")
                        .password(passwordEncoder.encode("1234")) // 1234를 bcrypt로 인코딩
                        .email("hyesoohan@example.com")
                        .role("ROLE_USER")
                        .build();
                memberRepository.save(dummy);
            }        };
    }
}
