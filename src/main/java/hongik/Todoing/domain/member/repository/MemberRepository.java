package hongik.Todoing.domain.member.repository;

import hongik.Todoing.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // Custom query methods can be defined here if needed
    // For example, findByUsername(String username) or findByEmail(String email)
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
}
