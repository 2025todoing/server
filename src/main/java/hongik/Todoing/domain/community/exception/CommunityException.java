package hongik.Todoing.domain.community.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class CommunityException extends GeneralException {
    public CommunityException(BaseErrorCode code) {
        super(code);
    }
}
