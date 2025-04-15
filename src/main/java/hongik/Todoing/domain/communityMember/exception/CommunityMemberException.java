package hongik.Todoing.domain.communityMember.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class CommunityMemberException extends GeneralException {

    public CommunityMemberException(BaseErrorCode code) {
        super(code);
    }
}
