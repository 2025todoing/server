package hongik.Todoing.domain.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRequestDto {
    private Long adminId;
    private String name;
    private String description;
}
