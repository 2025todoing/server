package hongik.Todoing.domain.friend.dto;

import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public record FriendRequestDTO(String friendEmail) {
    public FriendRequestDTO {
        if (friendEmail == null) {
            throw new GeneralException(ErrorStatus.FRIEND_REQUEST_IS_NULL);
        }
    }
}