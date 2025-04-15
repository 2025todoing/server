package hongik.Todoing.domain.todo.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class TodoException extends GeneralException {
    public TodoException(BaseErrorCode code) {
        super(code);
    }
}
