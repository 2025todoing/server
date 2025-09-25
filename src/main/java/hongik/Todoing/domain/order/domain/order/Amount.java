package hongik.Todoing.domain.order.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Amount {
    private int total; // 총 금액
    private int taxFree; // 면세 금액
    private int vat; // 부가세 금액
    private int point; // 포인트 사용 금액
    private int discount; // 할인 금액
    private int greenDeposit; // 환경부담금
}
