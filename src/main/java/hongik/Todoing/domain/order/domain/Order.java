package hongik.Todoing.domain.order.domain;

import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // 주문한 유저 아이디
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String tid;

    @Column(nullable = false)
    private String itemName;

    private Integer quantity;

    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.READY;

    // 결제 완료된 시간(승인 결제)
    private LocalDateTime approvedAt;

    /** --- 생성 관련 메서드 --- **/
    @Builder
    public Order (
            Long userId,
            String tid,
            String itemName,
            Integer quantity,
            Integer totalAmount,
            OrderStatus orderStatus ) {
        this.userId = userId;
        this.tid = tid;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }
}

