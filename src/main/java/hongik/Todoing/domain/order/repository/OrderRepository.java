package hongik.Todoing.domain.order.repository;

import hongik.Todoing.domain.order.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    Optional<Order> findByTid(String tid);

    Order findByPartnerOrderId(String partnerOrderId);

    boolean existsByPartnerOrderId(String partnerOrderId);


}
