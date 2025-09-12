package hongik.Todoing.domain.order.exception.passException;

import hongik.Todoing.domain.order.exception.orderException.OrderErrorCode;
import hongik.Todoing.domain.order.exception.orderException.OrderNotFoundException;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class PassNotValidException extends GeneralException {
    public static final GeneralException EXCEPTION =
            new PassNotValidException();
    public PassNotValidException() {
        super(PassErrorCode.PASS_NOT_VALID);
    }
}
