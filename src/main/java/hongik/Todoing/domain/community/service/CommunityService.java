package hongik.Todoing.domain.community.service;

import hongik.Todoing.domain.community.dto.request.CommunityRequestDto;
import hongik.Todoing.domain.community.dto.response.CommunityMemberDto;
import hongik.Todoing.domain.community.dto.response.CommunityResponseDto;

import java.util.List;

public interface CommunityService {

    CommunityResponseDto createCommunity(CommunityRequestDto request);
    List<CommunityResponseDto> getAllCommunities();
    CommunityResponseDto getCommunityByCommunityId(Long communityId);
    CommunityResponseDto updateCommunity(Long communityId,Long adminId, String description);
    void deleteCommunity(Long communityId, Long adminId);
    List<CommunityResponseDto> getCommunitiesByUserId(Long userId);
    void joinCommunity(Long userId, Long communityId);
    void leaveCommunity(Long userId, Long communityId);
    List<CommunityMemberDto> getCommunityMembers(Long communityId);

}
