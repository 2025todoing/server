package hongik.Todoing.domain.order.domain;

public enum OrderStatus {
    READY, // 결제 준비 상태
    APPROVED, // 결제 승인 상태
    CANCELLED, // 결제 취소 상태
    FAILED, // 결제 실패 상태
    REFUNDED // 환불 완료 상태
}
