package hongik.Todoing.domain.verification.domain;

import hongik.Todoing.domain.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    // 여기는 뭐로 해야하지?
    @Enumerated(EnumType.STRING)
    private VerificationType type;

}
