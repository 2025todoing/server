package hongik.Todoing.domain.chat.controller;

import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.chat.dto.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.domain.chat.service.ChatSessionService;
import hongik.Todoing.domain.chat.service.OpenAiService;
import hongik.Todoing.global.apiPayload.ApiResponse;
import hongik.Todoing.global.throttle.Throttle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiService openAiService;
    private final ChatSessionService chatSessionService;
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/setting")
    public ApiResponse<Void> setting(@AuthenticationPrincipal PrincipalDetails principal,
                          @RequestBody ChatSessionState requestDTO){

        chatSessionService.save(principal.getUsername(), requestDTO);
        return ApiResponse.onSuccess(null);

    }

    @Throttle(seconds = 10)
    @PostMapping("/message")
    public ApiResponse<ChatResponseDTO> chat(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody ChatRequestDTO requestDTO) {

        ChatResponseDTO chatResponseDTO = openAiService.ask(principal.getUsername(), requestDTO.getPrompt());
        return ApiResponse.onSuccess(chatResponseDTO);

    }
}
