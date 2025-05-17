package hongik.Todoing.domain.member.controller;

import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.dto.response.GetProfileDTO;
import hongik.Todoing.domain.member.dto.response.UpdateProfileDTO;
import hongik.Todoing.domain.member.service.MemberService;
import hongik.Todoing.global.apiPayload.ApiResponse;
import hongik.Todoing.global.apiPayload.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 내 프로필 조회
    @GetMapping("/me")
    public ApiResponse<GetProfileDTO> getMyProfile(
            @AuthenticationPrincipal PrincipalDetails principal) {
        Member member = principal.getMember();
        return ApiResponse.onSuccess(memberService.getProfile(member));
    }

    // 내 프로필 변경
    @PatchMapping("/me")
    public ApiResponse<Void> updateMyProfile (
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody UpdateProfileDTO dto
            ) {
        Member member = principal.getMember();
        memberService.updateProfile(member, dto);
        return ApiResponse.of(SuccessStatus._OK, null);
    }

}
