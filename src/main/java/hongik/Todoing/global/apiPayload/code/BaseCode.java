package hongik.Todoing.global.apiPayload.code;

import hongik.Todoing.global.apiPayload.code.errorDto.ReasonDTO;

public interface BaseCode {
    public ReasonDTO getReason();

    public ReasonDTO getReasonHttpStatus();
}
