package hongik.Todoing.domain.community.dto.response;

import lombok.Builder;

@Builder
public class CommunityMemberDto {
    private Long userId;
    private String userName;
}
