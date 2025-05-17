package hongik.Todoing.domain.friend.converter;

import hongik.Todoing.domain.friend.domain.Friend;
import hongik.Todoing.domain.friend.domain.FriendStatus;
import hongik.Todoing.domain.friend.dto.FriendResponseDTO;
import hongik.Todoing.domain.member.domain.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendConverter {
    public static Friend toEntity(Member me, Member friend) {
        return Friend.builder()
                .member(me)
                .friend(friend)
                .status(FriendStatus.ACCEPTED)
                .build();
    }

    public static FriendResponseDTO toFriendResponse(Friend friend) {
        Member target = friend.getFriend();
        return new FriendResponseDTO(target.getName(),
                friend.getStatus());
    }

    public static List<FriendResponseDTO> toFrienResponseDtoList(List<Friend> friends) {
        return friends.stream()
                .map(FriendConverter::toFriendResponse)
                .toList();
    }
}
