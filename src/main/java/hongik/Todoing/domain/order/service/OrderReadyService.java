package hongik.Todoing.domain.order.service;

import hongik.Todoing.Common.annotation.DomainService;
import hongik.Todoing.domain.order.adaptor.OrderAdaptor;
import hongik.Todoing.domain.order.domain.Order;
import hongik.Todoing.domain.order.domain.ProductCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@DomainService
@Transactional(readOnly = true)
public class OrderReadyService {

    private final OrderAdaptor orderAdaptor;

    public Order createOrder(Long userId, ProductCode product, int quantity) {
        // 주문을 생성합니다.
        Order order = Order.createReady(userId, product, quantity);

        // 주문을 저장합니다.
        return orderAdaptor.save(order);
    }


}
