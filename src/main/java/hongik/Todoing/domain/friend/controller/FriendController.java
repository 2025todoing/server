package hongik.Todoing.domain.friend.controller;

import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.friend.dto.FriendRequestDTO;
import hongik.Todoing.domain.friend.dto.FriendResponseDTO;
import hongik.Todoing.domain.friend.service.FriendService;
import hongik.Todoing.domain.todo.dto.response.TodoResponseDTO;
import hongik.Todoing.global.apiPayload.ApiResponse;
import hongik.Todoing.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;


    // 친구 추가 - email로 추가
    @PostMapping
    public ApiResponse<Void> addFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody FriendRequestDTO request
            ) {
        friendService.addFriend(principal.getMember(), request.friendEmail());
        return ApiResponse.onSuccess(null);
    }

    // 친구 내역 보기 - Id, name, status 반환
    @GetMapping
    public ApiResponse<List<FriendResponseDTO>> getMyFriends(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return ApiResponse.of(SuccessStatus._OK, friendService.getMyFriends(principal.getMember()));
    }

    // 친구 삭제하기 - id로 삭제
    @DeleteMapping("/{friendId}")
    public ApiResponse<Void> deleteFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long friendId
    ) {
        friendService.deleteFriend(principal.getMember(), friendId);
        return ApiResponse.onSuccess(null);
    }

    // 친구 차단하기 - id로 차단
    @PostMapping("/{friendId}/block")
    public ApiResponse<Void> blockFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long friendId
    ) {
        friendService.blockFriend(principal.getMember(), friendId);
        return ApiResponse.onSuccess(null);
    }

    // 친구 투두 리스트 보기 - id로 보기, 날짜에 따라서 todo 주기
    @GetMapping("/{friendId}/todos")
    public ApiResponse<List<TodoResponseDTO>> getFriendTodos(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long friendId
    ) {
        List<TodoResponseDTO> todoList = friendService.getFriendTodos(principal.getMember(), friendId);
        return ApiResponse.onSuccess(todoList);
    }
}
