package hongik.Todoing.domain.todo.domain;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todoReply.domain.TodoReply;
import hongik.Todoing.domain.verification.domain.Verification;
import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Todo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDate todoDate;
    private boolean isAiNeeded;
    private boolean isCompleted;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "verification_id")
    private Verification verification;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoReply> replies;

    public void updateComplete(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    public void updateTodo(String content, LocalDate todoDate, Label label) {
        this.content = content;
        this.todoDate = todoDate;
        this.label = label;
    }
}
