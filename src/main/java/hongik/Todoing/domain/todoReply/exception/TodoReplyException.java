package hongik.Todoing.domain.todoReply.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class TodoReplyException extends GeneralException {
    public TodoReplyException(BaseErrorCode code) {
        super(code);
    }
}
