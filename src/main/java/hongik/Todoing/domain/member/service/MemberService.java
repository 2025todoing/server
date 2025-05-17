package hongik.Todoing.domain.member.service;

import hongik.Todoing.domain.member.converter.MemberConverter;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.dto.response.GetProfileDTO;
import hongik.Todoing.domain.member.dto.response.UpdateProfileDTO;
import hongik.Todoing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    public GetProfileDTO getProfile(Member member) {
        return MemberConverter.toGetProfileDTO(member.getEmail(), member.getName());
    }


    @Transactional
    public void updateProfile(Member member, UpdateProfileDTO request) {
        if(request.name() != null)
            member.updateName(request.name());

        if(request.password() != null) {
            String encoded = passwordEncoder.encode(request.password());
            member.updatePassword(encoded);
        }

        memberRepository.save(member);

    }
}
