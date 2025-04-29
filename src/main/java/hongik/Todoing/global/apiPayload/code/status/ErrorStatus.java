package hongik.Todoing.global.apiPayload.code.status;

import hongik.Todoing.global.apiPayload.code.BaseErrorCode;
import hongik.Todoing.global.apiPayload.code.errorDto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "4000", "잘못된 요청입니다."),
    DATE_IS_NULL(HttpStatus.BAD_REQUEST, "4001", "시작 또는 종료 날짜가 null입니다."),
    DATE_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "4002", "시작 또는 종료 날짜가 유효하지 않습니다."),
    COMMUNITY_NAME_NULL(HttpStatus.BAD_REQUEST, "4003", "커뮤니티 이름이나 설명이 null입니다."),
    COMMUNITY_NAME_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "4004", "커뮤니티 이름이 중복되었습니다."),
    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "4010", "인증되지 않은 사용자입니다."),
    COMMUNITY_ADMIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "4011", "커뮤니티 관리자 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "4012", "토큰이 만료되었습니다."),
    TOKEN_IS_NOT_VALID(HttpStatus.UNAUTHORIZED, "4013", "토큰이 유효하지 않습니다."),
    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근이 금지된 사용자입니다."),
    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "404", "요청한 리소스를 찾을 수 없습니다."),
    COMMUNITY_NOT_FOUND(HttpStatus.NOT_FOUND, "4041", "커뮤니티를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "4042", "멤버를 찾을 수 없습니다."),
    COMMUNITY_ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "4043", "커뮤니티 관리자 정보를 찾을 수 없습니다."),
    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "409", "요청한 리소스와 충돌이 발생했습니다."),
    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 내부 오류입니다."),
    PASSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5001", "Oauth passing error."),
    // 503 SERVICE_UNAVAILABLE
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "503", "서비스를 사용할 수 없습니다."),
    // 504 GATEWAY_TIMEOUT
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "504", "서버가 응답하지 않습니다."),
    // 400 BAD_REQUEST
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "400", "잘못된 파라미터입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
