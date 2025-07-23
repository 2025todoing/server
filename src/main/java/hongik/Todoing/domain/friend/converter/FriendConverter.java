package hongik.Todoing.domain.friend.converter;

import hongik.Todoing.domain.friend.domain.Friend;
import hongik.Todoing.domain.friend.domain.FriendStatus;
import hongik.Todoing.domain.friend.dto.FriendResponseDTO;
import hongik.Todoing.domain.member.domain.Member;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendConverter {

    public static FriendResponseDTO toFriendResponse(Friend friend) {
        Member target = friend.getFriend();
        return new FriendResponseDTO(
                target.getId(),
                target.getName(),
                friend.getStatus());
    }

    public static List<FriendResponseDTO> toFrienResponseDtoList(List<Friend> friends) {
        return friends.stream()
                .map(FriendConverter::toFriendResponse)
                .toList();
    }
}
