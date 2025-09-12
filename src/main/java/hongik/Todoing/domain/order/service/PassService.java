package hongik.Todoing.domain.order.service;

import hongik.Todoing.Common.annotation.DomainService;
import hongik.Todoing.domain.order.adaptor.PassAdaptor;
import hongik.Todoing.domain.order.domain.order.Order;
import hongik.Todoing.domain.order.domain.pass.Pass;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PassService {

    private final PassAdaptor passAdaptor;

    // 패스 생성
    @Transactional
    public void createPass(Long userId, Order order) {
        // 패스 생성 로직을 구현합니다.
        // 예: 패스 엔티티를 생성하고 저장소에 저장

        Pass pass = new Pass(userId, order.getItemCode(), order.getId());

        passAdaptor.save(pass);

    }

    // 패스 조회

    // 패스 사용


}
