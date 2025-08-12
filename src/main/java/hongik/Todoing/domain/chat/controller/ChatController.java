package hongik.Todoing.domain.chat.controller;

import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.chat.dto.request.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.domain.chat.dto.response.ChatSubmitResponseDTO;
import hongik.Todoing.domain.chat.service.ChatDebounceService;
import hongik.Todoing.domain.chat.service.ChatSessionService;
import hongik.Todoing.domain.chat.service.OpenAiService;
import hongik.Todoing.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiService openAiService;
    private final ChatSessionService chatSessionService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ChatDebounceService chatDebounceService;

    @PostMapping("/setting")
    public ApiResponse<Void> setting(@AuthenticationPrincipal PrincipalDetails principal,
                          @RequestBody ChatSessionState requestDTO){

        chatSessionService.save(principal.getUsername(), requestDTO);
        return ApiResponse.onSuccess(null);

    }

    @PostMapping("/message")
    public ApiResponse<ChatSubmitResponseDTO> chat(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody ChatRequestDTO requestDTO) {

        ChatSubmitResponseDTO responseDTO =
                chatDebounceService.receiveUserMessage(principal.getUsername(), requestDTO.getMessages().get(0));
        return ApiResponse.onSuccess(responseDTO);

    }

    @GetMapping("/result")
    public ApiResponse<String> getResult(@AuthenticationPrincipal PrincipalDetails principal) {
        String result = chatDebounceService.getResult(principal.getUsername());
        return ApiResponse.onSuccess(result);
    }

}
