package hongik.Todoing.domain.order.service;

import hongik.Todoing.Common.annotation.DomainService;
import hongik.Todoing.domain.order.adaptor.KakaoPayAdaptor;
import hongik.Todoing.domain.order.adaptor.OrderAdaptor;
import hongik.Todoing.domain.order.adaptor.OrderUuidGenerator;
import hongik.Todoing.domain.order.domain.order.Order;
import hongik.Todoing.domain.order.domain.pass.ProductCode;
import hongik.Todoing.domain.order.dto.response.KakaoReadyResponse;
import hongik.Todoing.domain.order.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@DomainService
@Transactional(readOnly = true)
public class OrderService {

    private final OrderAdaptor orderAdaptor;
    private final OrderValidator orderValidator;
    private final OrderUuidGenerator uuidGenerator;
    private final KakaoPayAdaptor kakaoPayAdaptor;

    // Ready 상태
    @Transactional
    public KakaoReadyResponse readyOrder(Long userId, ProductCode product, int quantity) {

        // 가맹점 주문 번호 생성
        String partnerOrderId = uuidGenerator.forUser(userId, product.getName());

        // 주문을 생성합니다.
        Order order = Order.createReady(userId, product, quantity, partnerOrderId, orderValidator);

        // 카카오페이 결제 요청을 위한 준비
        KakaoReadyResponse response =  kakaoPayAdaptor.ready(order);

        // 주문에 결제 정보 저장
        orderAdaptor.saveWithTid(order, response.getTid());

        // 주문을 저장합니다.
        return response;
    }

    // 주문 승인
    @Transactional
    public Order approveOrder(Long userId, String tid, String pgToken) {
        // 주문을 찾습니다.
        Order order = orderAdaptor.findByTid(tid);

        // 주문 성공
        kakaoPayAdaptor.approve(order, pgToken);

        LocalDateTime now = LocalDateTime.now();
        return order.approve(now, orderValidator);
    }

    // 주문 승인 전 취소
    public void cancelOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.cancelBeforeApprove(now);
        orderAdaptor.save(order);
    }

    // 주문 실패
    public void failOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.failBeforeApprove(now);
        orderAdaptor.save(order);
    }

    // 주문 승인 후 환불
    public void refundOrder(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.refund(now);
        orderAdaptor.save(order);
    }
}
