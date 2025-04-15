package hongik.Todoing.domain.todo.domain;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todoReply.domain.TodoReply;
import hongik.Todoing.domain.verification.domain.Verification;
import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private String content;
    private LocalDate todoDate;
    private boolean isAiNeeded;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "verification_id")
    private Verification verification;

    @OneToMany(mappedBy = "todo")
    private List<TodoReply> replies;
}
