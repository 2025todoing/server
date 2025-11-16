package hongik.Todoing.domain.verification.domain;

import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationUsage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usageId;

    private Integer usageCount;

    @Column(name =  "user_id")
    private Long userId;

    public void increase() {
        if (usageCount == null) {
            usageCount = 0;
        }
        usageCount++;
    }
}
