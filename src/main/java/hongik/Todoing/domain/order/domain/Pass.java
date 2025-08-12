package hongik.Todoing.domain.order.domain;

import hongik.Todoing.domain.order.validator.PassValidator;
import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Pass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pass_id")
    private Long passId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 사용 횟수
    @Column(name = "used_count")
    private Integer usedCount;

    // 총 횟수
    @Column(name = "limit_count")
    private Integer limitCount;

    // 이용권 코드
    @Enumerated(EnumType.STRING)
    private ProductCode productCode;

    // 패스 상태 - ACTIVE / EXPIRED / REVOKED
    @Enumerated(EnumType.STRING)
    private PassStatus status;

    // 결제 아이디 추적용
    @Column(name = "order_id")
    private Long orderId;

    // Optimistic Locking 적용 - 버전 관리 중
    @Version
    private Long version;

    @Builder
    public Pass(
            Long userId,
            ProductCode productCode,
            Long orderId) {
        this.userId = userId;
        this.productCode = productCode;
        this.limitCount = productCode.getLimitCount();
        this.usedCount = 0; // 초기 사용 횟수는 0
        this.status = PassStatus.ACTIVE; // 기본 상태는 ACTIVE
        this.orderId = orderId; // 결제 아이디 설정
    }

    public Integer remainingCount() {
        return limitCount - usedCount;
    }

    // 사용 가능한 이용권인지 확인합니다.
    public void consume(Long userId, PassValidator passValidator) {
        passValidator.validPass(this, userId);
        this.usedCount++;
    }

    // 이미 결제가 완료되었는지 확인합니다.
    public Boolean isPaid() {
        return this.orderId != null;
    }

    // 사용자가 주문을 환불 시킵니다.
    public void revoke(Long userId, PassValidator passValidator) {
        passValidator.validRevoke(this);
        this.status = PassStatus.REVOKED;
    }
}
