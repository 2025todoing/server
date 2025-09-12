package hongik.Todoing.domain.member.exception;

import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class MemberNotFoundException extends GeneralException {
    public static final GeneralException EXCEPTION =
            new MemberNotFoundException();
    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}
