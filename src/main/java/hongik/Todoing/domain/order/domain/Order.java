package hongik.Todoing.domain.order.domain;

import hongik.Todoing.domain.order.exception.orderException.OrderNotValidException;
import hongik.Todoing.domain.order.validator.OrderValidator;
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
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 가맹점 주문 번호
    @Column(name = "partner_order_id", nullable = false, length = 100)
    private String partnerOrderId;

    // PG 거래 번호, 거래 후 지급됩니다.
    @Column(name = "tid", nullable = true, unique = true)
    private String tid;

    // 표시용 이름(영수증 내역 용)
    @Column(name = "item_name", nullable = false)
    private String itemName;

    // 수량 - 일반적으로 1개입니다.
    @Column(nullable = false)
    private Integer quantity;

    // 총 결제 금액
    @Column(nullable = false)
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.READY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    // 결제 완료된 시간(승인 결제)
    private LocalDateTime approvedAt;

    // 환불 시 시각
    private LocalDateTime cancelledAt;

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

    /** --- 카카오 페이 관련 결제를 생성합니다. --- **/
    // ready 주문을 생성합니다.
    public static Order createReady(
            Long userId,
            ProductCode productCode,
            int quantity,
            String partnerOrderId
    ) {
        Order order = new Order();
        order.userId = userId;
        order.partnerOrderId = partnerOrderId;
        order.itemName = productCode.getName();
        order.quantity = quantity;
        order.totalAmount = productCode.getPrice() * quantity;
        order.provider = PaymentProvider.KAKAO_PAY;
        return order;
    }

    // Kakao ready 후 받은 tid (최초 1회)
    public void setTid(String tid) {
        if(this.tid != null)
            throw OrderNotValidException.EXCEPTION;
        this.tid = tid;
    }

    // 승인 가능한 상태인지 확인
    public void enableApprove(OrderValidator validator) {
        validator.validApprove(this);
    }

    // 승인 처리
    public void approve(LocalDateTime now) {
        this.orderStatus = OrderStatus.APPROVED;
        this.approvedAt =now;
    }

    // 승인 전 취소
    public void cancelBeforeApprove(LocalDateTime now) {
        this.orderStatus = OrderStatus.CANCELLED;
        this.cancelledAt = now;
    }

    // 승인 후 환불
    public void refund(LocalDateTime now) {
        if(this.orderStatus != OrderStatus.APPROVED) {
            throw OrderNotValidException.EXCEPTION;
        }
        this.orderStatus = OrderStatus.REFUNDED;
        this.cancelledAt = now;
    }

}

