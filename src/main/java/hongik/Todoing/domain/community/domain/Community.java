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

    private String name;

    @OneToMany(mappedBy = "community")
    private List<CommunityMember> communityMembers;

    @OneToMany(mappedBy = "community")
    private List<Chat> chats;
}
