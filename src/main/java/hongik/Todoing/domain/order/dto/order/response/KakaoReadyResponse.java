package hongik.Todoing.domain.order.dto.order.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoReadyResponse {
    private String tid;
    // pc 연결 용
    private String next_redirect_pc_url;
    // 모바일 연결 용
    private String next_redirect_mobile_url;
    private String created_at;

}
