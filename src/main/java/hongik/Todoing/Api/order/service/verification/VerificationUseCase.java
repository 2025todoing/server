package hongik.Todoing.Api.order.service.verification;

import hongik.Todoing.Common.annotation.UseCase;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.verification.domain.VerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class VerificationUseCase {

    // 투두 인증 사용
    // 투두 content + verificationType
    // 사용 시 pass 차감
    @Transactional
    public void useVerification(Long userId, Todo todo, VerificationType type) {
        // 투두 인증 사용 로직 구현

        // todo.getContent();

        // verificationService.useVerification(userId, todo, type);

        // 여기서 만약 잘 되면 패스 사용, 아니면 예외처리 - Transaction 필요함

        // passService.usePass(userId, passId);

        // 성공하면 true이고 todo의 isVerified를 true로 바꾼다.
        // return true;
    }

}
