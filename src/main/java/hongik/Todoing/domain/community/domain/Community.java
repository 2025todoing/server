package hongik.Todoing.domain.community.domain;


import hongik.Todoing.domain.chat.domain.Chat;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import hongik.Todoing.global.common.BaseEntity;
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
public class Community extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "community")
    private List<CommunityMember> communityMembers;

    @OneToMany(mappedBy = "community")
    private List<Chat> chats;

    public void updateDescription(String description) {
        this.description = description;
    }
}
