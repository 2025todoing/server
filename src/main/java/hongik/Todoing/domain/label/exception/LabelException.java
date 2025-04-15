package hongik.Todoing.domain.label.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class LabelException extends GeneralException {
    public LabelException(BaseErrorCode code) {
        super(code);
    }
}
