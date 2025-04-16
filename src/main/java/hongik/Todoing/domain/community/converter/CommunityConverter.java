package hongik.Todoing.domain.community.converter;

import hongik.Todoing.domain.community.domain.Community;
import hongik.Todoing.domain.community.dto.request.CommunityRequestDto;
import hongik.Todoing.domain.community.dto.response.CommunityMemberDto;
import hongik.Todoing.domain.community.dto.response.CommunityResponseDto;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommunityConverter {

    public Community toEntity(CommunityRequestDto request) {
        return Community.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CommunityResponseDto toDTO(Community community) {
        return CommunityResponseDto.builder()
                .communityId(community.getCommunityId())
                .name(community.getName())
                .description(community.getDescription())
                .localDate(community.getCreatedAt())
                .build();
    }

    public List<CommunityResponseDto> toDTOList(List<Community> communities) {
        return communities.stream()
                .map(this::toDTO)
                .toList();
    }
}
