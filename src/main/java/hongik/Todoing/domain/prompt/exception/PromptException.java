package hongik.Todoing.domain.prompt.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class PromptException extends GeneralException {

    public PromptException(BaseErrorCode code) {
        super(code);
    }
}
