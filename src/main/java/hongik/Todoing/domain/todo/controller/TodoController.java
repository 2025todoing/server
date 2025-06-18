package hongik.Todoing.domain.todo.controller;


import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.domain.chat.service.ChatSessionService;
import hongik.Todoing.domain.todo.dto.request.ChatTodoCreateRequestDTO;
import hongik.Todoing.domain.todo.dto.request.TodoCreateRequestDTO;
import hongik.Todoing.domain.todo.dto.request.TodoUpdateRequestDTO;
import hongik.Todoing.domain.todo.dto.response.TodoResponseDTO;
import hongik.Todoing.domain.todo.service.ChatTodoService;
import hongik.Todoing.domain.todo.service.SelfTodoService;
import hongik.Todoing.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final SelfTodoService selfTodoService;
    private final ChatSessionService chatSessionService;
    private final ChatTodoService chatTodoService;

    // 투두리스트 만들기 - self
    @PostMapping
    public ApiResponse<Void> createTodo(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody TodoCreateRequestDTO requestDTO
            ) {
        selfTodoService.createTodo(principal.getMember(), requestDTO);
        return ApiResponse.onSuccess(null);
    }

    // 투두리스트 보기 - 날짜로
    @GetMapping
    public ApiResponse<List<TodoResponseDTO>> getMyToos(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date
            ) {
        List<TodoResponseDTO> todos = selfTodoService.getTodosByDate(principal.getMember(), date);
        return ApiResponse.onSuccess(todos);
    }


    // 투두 삭제하기
    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodo(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long todoId
    ) {
        selfTodoService.deleteTodo(principal.getMember(), todoId);
        return ApiResponse.onSuccess(null);
    }


    // 투두 수정하기
    @PutMapping("/{todoId}")
    public ApiResponse<Void> updateTodo(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long todoId,
            @RequestBody TodoUpdateRequestDTO requestDTO
    ) {
        selfTodoService.updateTodo(principal.getMember(), todoId, requestDTO);
        return ApiResponse.onSuccess(null);
    }

    // 투두 완료-미완료 토글
    @PutMapping("/{todoId}/toggle")
    public ApiResponse<Void> toggleComplete(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long todoId
    ) {
        selfTodoService.toggleTodo(principal.getMember(), todoId);
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/chat")
    public ApiResponse<Void> createTodoWithChat(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody ChatTodoCreateRequestDTO requestDTO){

        ChatSessionState sessionState = chatSessionService.get(principal.getUsername());
        if(sessionState == null ){
            return ApiResponse.onSuccess(null);
        }
        chatTodoService.createTodo(principal.getMember(), requestDTO, sessionState);
        return ApiResponse.onSuccess(null);

    }



}
