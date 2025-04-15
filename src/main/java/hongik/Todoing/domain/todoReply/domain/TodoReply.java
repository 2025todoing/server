package hongik.Todoing.domain.todoReply.domain;

import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todo.domain.Todo;
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
public class TodoReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String content;

    // 댓글 작성자의 역할을 구분하는 필드임.
    @Enumerated(EnumType.STRING)
    private ReplyType replyType;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
