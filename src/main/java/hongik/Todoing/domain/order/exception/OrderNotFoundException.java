package hongik.Todoing.domain.order.exception;

import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;

public class OrderNotFoundException extends GeneralException {

    public static final GeneralException EXCEPTION =
            new OrderNotFoundException();
    public OrderNotFoundException() {
        super(OrderErrorCode.ORDER_NOT_FOUND);
    }
}
