package hongik.Todoing.domain.login;

import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberEntity = memberRepository.findByName(username);
        if(memberEntity.isPresent()) {
            Member member = memberEntity.get();
            //return new PrincipalDetails(member.getName(), member.getPassword(), member.getRole());
            return new PrincipalDetails(member);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
