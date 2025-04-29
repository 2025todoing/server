package hongik.Todoing.domain.member.domain;

import hongik.Todoing.domain.chat.domain.Chat;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import hongik.Todoing.domain.prompt.domain.PromptInput;
import hongik.Todoing.domain.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private String email;

    private String role; // ROLE_USER, ROLE_ADMIN

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromptInput> promptInputs;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos;

    @OneToMany(mappedBy = "member")
    private List<CommunityMember> communityMembers;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chats;

    public List<String> getRoleList() {
        if(!this.role.isEmpty()) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
