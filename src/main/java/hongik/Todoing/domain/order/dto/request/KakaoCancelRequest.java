package hongik.Todoing.domain.order.dto.request;

import lombok.Getter;

@Getter
public class KakaoCancelRequest {
    private String tid; // 결제 고유 번호
    private Long userId; // 결제자 아이디
}
