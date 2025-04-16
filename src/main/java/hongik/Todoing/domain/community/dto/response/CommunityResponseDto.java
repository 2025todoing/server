package hongik.Todoing.domain.community.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CommunityResponseDto {
    private Long communityId;
    private String name;
    private String description;
    private LocalDateTime localDate;
}
