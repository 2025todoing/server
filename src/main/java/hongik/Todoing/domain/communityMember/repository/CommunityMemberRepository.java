package hongik.Todoing.domain.communityMember.repository;

import hongik.Todoing.domain.community.domain.Community;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import hongik.Todoing.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

    List<CommunityMember> findByCommunity(Community community);
    Optional<CommunityMember> finByCommunityAndMember(Community community, Member member);
    boolean existsByCommunityAndMember(Community community, Member memberId);
    List<CommunityMember> findByMember(Member member);
}
