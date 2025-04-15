package hongik.Todoing.domain.verification.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class VerificationException extends GeneralException {

    public VerificationException(BaseErrorCode code) {
        super(code);
    }
}
