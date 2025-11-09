package hongik.Todoing.domain.order.repository;

import hongik.Todoing.domain.order.domain.pass.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassRepository extends JpaRepository<Pass, Long> {
    List<Pass> findByUserId(Long userId);
    Optional<Pass> findByOrderId(Long orderId);

    // 패스들 중 선택해서 사용 할 예정


}
