package hongik.Todoing.domain.verification.dto;

import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.verification.Adaptor.VerificationAdaptor;
import hongik.Todoing.domain.verification.domain.Verification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationResponse {

    private Long verificationId;
    private Long todoId;
    private Long userId;
    private double confidence;
    private String message;

    public static VerificationResponse from(Verification verification, Todo todo, boolean success, double confidence) {
        return VerificationResponse.builder()
                .verificationId(verification.getVerificationId())
                .todoId(todo.getTodoId() )
                .userId(todo.getMemberId())
                .confidence(confidence)
                .message(success ? "Verification succeeded." : "Verification failed.")
                .build();
    }


}
