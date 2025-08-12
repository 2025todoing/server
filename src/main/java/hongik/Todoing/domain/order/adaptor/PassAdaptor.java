package hongik.Todoing.domain.order.adaptor;

import hongik.Todoing.Common.annotation.Adaptor;
import hongik.Todoing.domain.order.domain.Pass;
import hongik.Todoing.domain.order.exception.passException.PassNotFoundException;
import hongik.Todoing.domain.order.repository.PassRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class PassAdaptor {

    private final PassRepository passRepository;

    public Pass save(Pass pass) {
        return passRepository.save(pass);
    }

    public Pass findById(Long passId) {
        return passRepository
                .findById(passId)
                .orElseThrow(() -> PassNotFoundException.EXCEPTION);
    }

    public List<Pass> findByUserId(Long userId) {
        return passRepository.finByUserId(userId);
    }

    public Pass findByOrderId(Long orderId) {
        return passRepository.findByOrderId(orderId)
                .orElseThrow(() -> PassNotFoundException.EXCEPTION);
    }

}
