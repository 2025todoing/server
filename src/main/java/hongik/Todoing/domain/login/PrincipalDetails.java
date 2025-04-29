package hongik.Todoing.domain.login;

import hongik.Todoing.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class PrincipalDetails implements UserDetails {

    private final Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 Member 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoleList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 예를 들어 사이트에서 일정 기간동안 로그인 안하면 -> 휴면 계정
        // member Entity의 field에 TimeStamp 추가해서
        // 현재 시간 - TimeStamp > 1년 이면 false 리턴

        return true;
    }
}
