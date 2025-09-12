package hongik.Todoing.domain.member.validator;

import hongik.Todoing.Common.annotation.Validator;
import hongik.Todoing.domain.member.exception.MemberErrorCode;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;

@Validator
@RequiredArgsConstructor
public class MemberValidator {

    public void validCreate(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new GeneralException(MemberErrorCode.EMAIL_NOT_VALID);
        }
        if (password == null || password.isEmpty()) {
            throw new GeneralException(MemberErrorCode.PASSWORD_CANNOT_BE_NULL);
        }
        // 추가적인 유효성 검사 로직을 여기에 작성할 수 있습니다.
    }
}
