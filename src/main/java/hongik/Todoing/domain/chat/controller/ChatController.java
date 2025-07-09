package hongik.Todoing.domain.chat.controller;

import hongik.Todoing.domain.auth.util.PrincipalDetails;
import hongik.Todoing.domain.chat.dto.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.domain.chat.service.ChatSessionService;
import hongik.Todoing.domain.chat.service.OpenAiService;
import hongik.Todoing.global.apiPayload.ApiResponse;
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
    @PostMapping("/message")
    public ApiResponse<ChatResponseDTO> chat(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody ChatRequestDTO requestDTO) {

        System.out.println("✅ System.out: message API 호출됨");
        log.info("✅ Slf4j 로그: message API 호출됨");

        String userId = principal.getUsername();
        String throttleKey = "throttle:" + userId;

        System.out.println(userId);


        // 쓰로틀링 1초 동안 한 번만 허용 (Redis key 존재하면 거절)
        Boolean isAllowed = stringRedisTemplate.opsForValue()
                .setIfAbsent(throttleKey, "2", Duration.ofSeconds(2));

        log.info("[Chat] Throttle check for {} = {}", throttleKey, isAllowed);

        if (Boolean.FALSE.equals(isAllowed)) {
            log.info("[Chat] Throttle rejected for {}", userId);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "너무 자주 요청하고 있어요. 잠시만 기다려주세요!");
        }

        ChatResponseDTO chatResponseDTO = openAiService.ask(principal.getUsername(), requestDTO.getPrompt());
        return ApiResponse.onSuccess(chatResponseDTO);

    }
}
