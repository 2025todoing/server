package hongik.Todoing.domain.order.dto.order.request;

import hongik.Todoing.domain.order.domain.pass.ProductCode;
import lombok.Getter;

@Getter
public class KakaoReadyRequest {
    private final Long userId; // 결제자 아이디
    private final ProductCode productCode; // 상품 코드
    private final int quantity; // 수량
    private final String partnerOrderId; // 가맹점 주문 번호


    public KakaoReadyRequest(Long userId, ProductCode productCode, int quantity, String partnerOrderId) {
        this.userId = userId;
        this.productCode = productCode;
        this.quantity = quantity;
        this.partnerOrderId = partnerOrderId;
    }
}
