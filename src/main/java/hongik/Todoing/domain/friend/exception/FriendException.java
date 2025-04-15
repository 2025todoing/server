package hongik.Todoing.domain.friend.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class FriendException extends GeneralException {
    public FriendException(BaseErrorCode code) {
        super(code);
    }
}
