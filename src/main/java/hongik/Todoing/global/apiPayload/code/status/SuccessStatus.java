package hongik.Todoing.global.apiPayload.code.status;

import hongik.Todoing.global.apiPayload.code.BaseCode;
import hongik.Todoing.global.apiPayload.code.errorDto.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),
    _AuthorIZED(HttpStatus.OK, "COMMON202", "인증되었습니다."),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build()
                ;
    }

    @Override
    public ReasonDTO getReasonHttpStatus(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
