package hongik.Todoing.domain.member.exception;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.code.errorDto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member_404_1", "해당 유저를 찾을 수 없습니다."),
    MEMBER_NOT_VALID(HttpStatus.BAD_REQUEST, "Member_400_1", "유효하지 않은 유저입니다."),
    MEMBER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "Member_401_1", "인증되지 않은 유저입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).httpStatus(httpStatus).build();
    }
}
