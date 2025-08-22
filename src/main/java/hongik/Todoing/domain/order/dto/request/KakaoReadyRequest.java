package hongik.Todoing.domain.order.dto.request;

import hongik.Todoing.domain.order.domain.pass.ProductCode;
import lombok.Getter;

@Getter
public class KakaoReadyRequest {
    private Long userId; // 결제자 아이디
    private ProductCode productCode; // 상품 코드
}
