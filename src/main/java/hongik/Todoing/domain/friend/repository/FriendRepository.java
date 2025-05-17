package hongik.Todoing.domain.friend.repository;

import hongik.Todoing.domain.friend.domain.Friend;
import hongik.Todoing.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByMemberAndFriend(Member member, Member friend);
    Friend findByMemberAndFriend (Member member, Member friend);
    List<Friend> findAllByMember(Member member);
}
