package hongik.Todoing.domain.member.domain;

import hongik.Todoing.domain.chat.domain.Chat;
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
@Table(name = "`user`")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    public List<String> getRoleList() {
        if(!this.role.isEmpty()) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
