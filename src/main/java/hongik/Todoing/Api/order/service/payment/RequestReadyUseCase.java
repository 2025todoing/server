package hongik.Todoing.Api.order.service.payment;

import hongik.Todoing.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestReadyUseCase {

    /*
    * Service: 도메인 레이어
    * UseCase: Application 레이어
    */


    private final OrderService orderService;


}
