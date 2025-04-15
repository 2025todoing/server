package hongik.Todoing.global.apiPayload.code;

import hongik.Todoing.global.apiPayload.code.errorDto.ErrorReasonDTO;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
