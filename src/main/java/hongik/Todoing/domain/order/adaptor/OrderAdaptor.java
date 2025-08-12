package hongik.Todoing.domain.order.adaptor;

import hongik.Todoing.domain.order.repository.OrderRepository;
import hongik.Todoing.domain.order.domain.Order;
import hongik.Todoing.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderAdaptor {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);
    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order findByTid(String tid) {
        return orderRepository.findByTid(tid)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);
    }


}
