package hongik.Todoing.domain.member.domain;

import hongik.Todoing.domain.chat.domain.Chat;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import hongik.Todoing.domain.prompt.domain.PromptInput;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private String nickname;

    private String email;
    private EmailType emailType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromptInput> promptInputs;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos;

    @OneToMany(mappedBy = "member")
    private List<CommunityMember> communityMembers;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chats;
}
