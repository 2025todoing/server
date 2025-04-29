package hongik.Todoing.domain.prompt.domain;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.prompt.exception.PromptException;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptInput extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promptInputId;

    private Level level;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    public Period getPeriod() {
        if(startDate == null || endDate == null) {
            throw new PromptException(ErrorStatus.DATE_IS_NULL);
        }

        return Period.between(startDate, endDate);
    }
}
