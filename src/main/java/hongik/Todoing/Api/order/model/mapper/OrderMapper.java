package hongik.Todoing.Api.order.model.mapper;

import hongik.Todoing.Common.annotation.Mapper;
import hongik.Todoing.domain.order.adaptor.OrderAdaptor;
import hongik.Todoing.domain.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderAdaptor orderAdaptor;

    @Transactional(readOnly = true)
    public OrderResponse toOrderResponse(String tid) {
        Order order = orderAdaptor.findByTid(tid);

        // 여기서 OrderResponse 객체를 생성하고 필요한 필드를 설정합니다.
    }

    @Transactional(readOnly = true)
    public OrderResponse toOrderResponse(Order order) {

    }

    @Transactional(readOnly = true)
    public CreateOrderResponse toCreateOrderResponse(String tid) {
        Order order = orderAdaptor.findByTid(tid);

    }
}
