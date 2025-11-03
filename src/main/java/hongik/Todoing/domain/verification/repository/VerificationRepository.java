package hongik.Todoing.domain.verification.repository;

import hongik.Todoing.domain.verification.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

}
