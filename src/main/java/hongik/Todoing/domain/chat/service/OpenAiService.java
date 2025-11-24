package hongik.Todoing.domain.chat.service;
import hongik.Todoing.domain.chat.dto.request.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.response.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.global.prompt.SystemPromptLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ChatSessionService sessionService;
    private final SystemPromptLoader systemPromptLoader;

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;


    public ChatResponseDTO ask(String userId, List<ChatRequestDTO.Message> messages) {

        ChatSessionState session = sessionService.get(userId);

        String url = "https://api.openai.com/v1/chat/completions";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);


        List<Map<String, Object>> fullMessages = new ArrayList<>();

        // 1. 캐릭터/말투 정의 (Persona)
        fullMessages.add(Map.of(
                "role", "system",
                "content", systemPromptLoader.get("persona")
        ));

        // 2. JSON 포맷 강제 (Format)
        fullMessages.add(Map.of(
                "role", "system",
                "content", systemPromptLoader.get("format")
        ));

        // 3. 세션 정보 (Session)
        fullMessages.add(Map.of(
                "role", "system",
                "content", systemPromptLoader.getSessionFormatted(
                        session.getCategory(),
                        session.getLevel(),
                        session.getStartDate(),
                        session.getEndDate()
                )
        ));

        // 4. 기존 대화 (user / assistant)
        fullMessages.addAll(
                messages.stream().map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("role", msg.getRole());
                    map.put("content", msg.getContent());
                    return map;
                }).collect(Collectors.toList())
        );


        Map<String, Object> body = new HashMap<>();
        body.put("model","gpt-5");
        body.put("messages", fullMessages);

        HttpEntity<Map<String, Object>> entity = new
                HttpEntity<>(body, headers);

        System.out.println("=== Final Prompt Sent to OpenAI ===");
        fullMessages.forEach(msg -> System.out.println(msg.get("role") + ": " + msg.get("content")));
        System.out.println("===================================");

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> firstMessage = (Map<String, Object>) choices.get(0).get("message");
            return new ChatResponseDTO((String) firstMessage.get("content"));
        } else {
            return new ChatResponseDTO("응답 없음.");
        }
    }
}