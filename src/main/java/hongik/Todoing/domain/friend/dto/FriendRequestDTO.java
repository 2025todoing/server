package hongik.Todoing.domain.friend.dto;

import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public record FriendRequestDTO(Long friendId) {
    public FriendRequestDTO {
        if (friendId == null) {
            throw new GeneralException(ErrorStatus.FRIEND_REQUEST_IS_NULL);
        }
    }
}
