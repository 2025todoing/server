package hongik.Todoing.domain.community.service;

import hongik.Todoing.domain.community.converter.CommunityConverter;
import hongik.Todoing.domain.community.domain.Community;
import hongik.Todoing.domain.community.dto.request.CommunityRequestDto;
import hongik.Todoing.domain.community.dto.response.CommunityMemberDto;
import hongik.Todoing.domain.community.dto.response.CommunityResponseDto;
import hongik.Todoing.domain.community.exception.CommunityException;
import hongik.Todoing.domain.community.repository.CommunityRepository;
import hongik.Todoing.domain.communityMember.domain.CommunityMember;
import hongik.Todoing.domain.communityMember.domain.CommunityRole;
import hongik.Todoing.domain.communityMember.repository.CommunityMemberRepository;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.repository.MemberRepository;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final CommunityConverter communityConverter;
    private final MemberRepository memberRepository;

    private static final Logger log = LoggerFactory.getLogger(CommunityServiceImpl.class);


    /*
    커뮤니티 기능은 필요 없을 예정
     */
    @Override
    public CommunityResponseDto createCommunity(CommunityRequestDto request) {

        if (request.getName() == null || request.getDescription() == null) {
            throw new CommunityException(ErrorStatus.COMMUNITY_NAME_NULL);
        }

        // 커뮤니티 이름이 동일하면 생성 불가능
        if(communityRepository.existsByName(request.getName())) {
            throw new CommunityException(ErrorStatus.COMMUNITY_NAME_IS_DUPLICATED);
        }

        // 커뮤니티 생성자 ID로 멤버 조회
        // 커뮤니티 생성자 ID로 멤버가 존재하지 않으면 예외 발생
        Member adminMember = memberRepository.findById(request.getAdminId())
                .orElseThrow(() -> new CommunityException(ErrorStatus.MEMBER_NOT_FOUND));

        // 커뮤니티 생성
        Community community = communityConverter.toEntity(request);
        communityRepository.save(community);

        //커뮤니티 생성자가 커뮤니티 Admin으로 됨
        CommunityMember admin = CommunityMember.builder()
                .member(adminMember)
                .community(community)
                .role(CommunityRole.ADMIN)
                .build();

        communityMemberRepository.save(admin);

        log.debug("커뮤니티 생성 디버그 완료" );

        // 커뮤니티 DTO로 변환 후 반환
        return communityConverter.toDTO(community);

    }

    @Override
    public List<CommunityResponseDto> getAllCommunities() {

        // 모든 커뮤니티 조회
        List<Community> communities = communityRepository.findAllByOrderByCreatedAtDesc();

        log.debug("Get All Communities : ");

        // 커뮤니티 DTO로 변환 후 반환
        return communityConverter.toDTOList(communities);
    }

    @Override
    public CommunityResponseDto getCommunityByCommunityId(Long communityId) {

        // 커뮤니티 ID로 커뮤니티 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_NOT_FOUND));
        // 커뮤니티 DTO로 변환 후 반환
        return communityConverter.toDTO(community);

    }

    // 오직 설명만 수정 가능하게
    @Override
    public CommunityResponseDto updateCommunity(Long communityId,Long adminId, String description) {
        Community community = checkCommunityAndAdmin(communityId, adminId);

        // 커뮤니티 수정
        community.updateDescription(description);

        // 커뮤니티 저장
        communityRepository.save(community);

        return communityConverter.toDTO(community);
    }


    // 로그인 로직 구현 후 jwt 인증 필요 -> 오직 ADMIN만 생성 가능하게.
    @Override
    public void deleteCommunity(Long communityId, Long adminId) {

        Community communtiy = checkCommunityAndAdmin(communityId, adminId);

        // 커뮤니티 삭제
        communityRepository.delete(communtiy);


    }

    @Override
    public List<CommunityResponseDto> getCommunitiesByUserId(Long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.MEMBER_NOT_FOUND));

        // 커뮤니티 멤버 ID로 커뮤니티 조회
        List<CommunityMember> communityMembers = communityMemberRepository.findByMember(member);

        // 커뮤니티 DTO로 변환 후 반환
        List<Community> communities = communityMembers.stream()
                .map(CommunityMember::getCommunity)
                .toList();

        return communityConverter.toDTOList(communities);

    }

    @Override
    public void joinCommunity(Long userId, Long communityId) {

        // 커뮤니티 가입
        // 커뮤니티 ID로 커뮤니티 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_NOT_FOUND));

        // 커뮤니티 생성자 ID로 멤버가 존재하지 않으면 예외 발생
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.MEMBER_NOT_FOUND));

        communityMemberRepository.save(CommunityMember.builder()
                .member(member)
                .community(community)
                .role(CommunityRole.MEMBER)
                .build());

        log.debug("Join Community : " + community.getName());
    }

    @Override
    public void leaveCommunity(Long userId, Long communityId) {
        // 커뮤니티 나가기
        // 커뮤니티 ID로 커뮤니티 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_NOT_FOUND));

        // 커뮤니티 생성자 ID로 멤버가 존재하지 않으면 예외 발생
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.MEMBER_NOT_FOUND));

        // 커뮤니티 멤버 조회
        CommunityMember communityMember = communityMemberRepository.findByCommunityAndMember(community, member)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_ADMIN_NOT_FOUND));

        // 커뮤니티 멤버 삭제
        communityMemberRepository.delete(communityMember);

        log.debug("Leave Community : " + community.getName());

    }

    @Override
    public List<CommunityMemberDto> getCommunityMembers(Long communityId) {

        // 커뮤니티 사용자 조회
        // 커뮤니티 ID로 커뮤니티 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_NOT_FOUND));

        // 커뮤니티 멤버 조회
        List<CommunityMember> communityMembers = communityMemberRepository.findByCommunity(community);

        // 커뮤니티 멤버 DTO로 변환 후 반환

        return communityMembers.stream()
                .map(communityMember -> CommunityMemberDto.builder()
                        .memberId(communityMember.getMember().getId())
                        .userName(communityMember.getMember().getName())
                        .build())
                .toList();
    }

    private Community checkCommunityAndAdmin(Long communityId, Long adminId) {
        // 커뮤니티 ID로 커뮤니티 조회
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_NOT_FOUND));


        // 커뮤니티 생성자 ID로 멤버가 존재하지 않으면 예외 발생
        Member adminMember = memberRepository.findById(adminId)
                .orElseThrow(() -> new CommunityException(ErrorStatus.MEMBER_NOT_FOUND));

        // 커뮤니티 생성자와 수정 요청한 멤버가 다르면 예외 발생
        CommunityMember communityAdmin = communityMemberRepository.findByCommunityAndMember(community, adminMember)
                .orElseThrow(() -> new CommunityException(ErrorStatus.COMMUNITY_ADMIN_NOT_FOUND));

        // 커뮤니티 생성자와 수정 요청한 멤버가 ADMIN이 아니면 예외 발생
        if(communityAdmin.getRole() != CommunityRole.ADMIN) {
            throw new CommunityException(ErrorStatus.COMMUNITY_ADMIN_UNAUTHORIZED);
        }

        log.debug("Check Community's Admin And Request User's Id is :  " + communityAdmin.getRole());

        return community;
    }
}
