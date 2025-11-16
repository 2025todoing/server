package hongik.Todoing.domain.verification.repository;

import hongik.Todoing.domain.verification.domain.VerificationUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationUsageRepository extends JpaRepository<VerificationUsage, Long> {

    Optional<VerificationUsage> findByUserId(Long userId);
}
