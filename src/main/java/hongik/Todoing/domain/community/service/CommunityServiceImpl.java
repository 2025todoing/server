package hongik.Todoing.domain.community.service;

import hongik.Todoing.domain.community.converter.CommunityConverter;
import hongik.Todoing.domain.community.dto.request.CommunityRequestDto;
import hongik.Todoing.domain.community.dto.response.CommunityMemberDto;
import hongik.Todoing.domain.community.dto.response.CommunityResponseDto;
import hongik.Todoing.domain.community.repository.CommunityRepository;
import hongik.Todoing.domain.communityMember.repository.CommunityMemberRepository;
import hongik.Todoing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final CommunityConverter communityConverter;
    private final MemberRepository memberRepository;

    @Override
    public CommunityResponseDto createCommunity(CommunityRequestDto request) {
        return null;
    }

    @Override
    public List<CommunityResponseDto> getAllCommunities() {
        return null;
    }

    @Override
    public CommunityResponseDto getCommunityByCommunityId(Long communityId) {
        return null;
    }

    @Override
    public CommunityResponseDto updateCommunity(Long communityId, CommunityRequestDto request) {
        return null;
    }

    @Override
    public void deleteCommunity(Long communityId) {

    }

    @Override
    public List<CommunityResponseDto> getCommunitiesByUserId(Long userId) {
        return null;
    }

    @Override
    public void joinCommunity(Long userId, Long communityId) {

    }

    @Override
    public void leaveCommunity(Long userId, Long communityId) {

    }

    @Override
    public List<CommunityMemberDto> getCommunityMembers(Long communityId) {
        return null;
    }
}
