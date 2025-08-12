package hongik.Todoing.domain.order.repository;

import hongik.Todoing.domain.order.domain.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    List<Pass> finByUserId(Long userId);
    Optional<Pass> findByOrderId(Long orderId);

}
