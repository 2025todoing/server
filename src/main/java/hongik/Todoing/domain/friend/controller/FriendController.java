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


    // 친구 추가
    @PostMapping
    public ApiResponse<Void> addFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody FriendRequestDTO request
            ) {
        Long friendId = friendService.getFriendId(request.friendEmail());
        friendService.addFriend(principal.getMember(), friendId);
        return ApiResponse.onSuccess(null);
    }

    // 친구 내역 보기
    @GetMapping
    public ApiResponse<List<FriendResponseDTO>> getMyFriends(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return ApiResponse.of(SuccessStatus._OK, friendService.getMyFriends(principal.getMember()));
    }

    // 친구 삭제하기
    @DeleteMapping("/{friendEmail}")
    public ApiResponse<Void> deleteFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable String friendEmail
    ) {
        Long friendId = friendService.getFriendId(friendEmail);
        friendService.deleteFriend(principal.getMember(), friendId);
        return ApiResponse.onSuccess(null);
    }

    // 친구 차단하기
    @PostMapping("/{friendEmail}/block")
    public ApiResponse<Void> blockFriend(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable String friendEmail
    ) {
        Long friendId = friendService.getFriendId(friendEmail);
        friendService.blockFriend(principal.getMember(), friendId);
        return ApiResponse.onSuccess(null);
    }

    // 친구 투두 리스트 보기
    @PostMapping("/{friendEmail}/todos")
    public ApiResponse<List<TodoResponseDTO>> getFriendTodos(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable String friendEmail
    ) {
        Long friendId = friendService.getFriendId(friendEmail);
        List<TodoResponseDTO> todoList = friendService.getFriendTodos(principal.getMember(), friendId);
        return ApiResponse.onSuccess(todoList);
    }
}
