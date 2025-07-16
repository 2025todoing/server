package hongik.Todoing.domain.chat.service;
import hongik.Todoing.domain.chat.dto.ChatRequestDTO;
import hongik.Todoing.domain.chat.dto.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.global.prompt.SystemPromptLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

        String systemPrompt = String.format(
                systemPromptLoader.loadPrompt(),
                session.getCategory(),
                session.getLevel(),
                session.getStartDate(),
                session.getEndDate()
        );

        //시스템 프롬프트 추가
        Map<String, Object> systemMessage = Map.of(
                "role", "system",
                "content", systemPrompt);

        List<Map<String, Object>> convertedMessages = messages.stream()
                .map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("role", msg.getRole());
                    map.put("content", msg.getContent());
                    return map;
                })
                .collect(Collectors.toList());

        // 최종 messages 구성: system + user/assistant 대화들
        List<Map<String, Object>> fullMessages = new ArrayList<>();
        fullMessages.add(systemMessage);
        fullMessages.addAll(convertedMessages);

        // 로그 출력
        System.out.println("[ChatGPT 요청 메시지]");
        for (Map<String, Object> msg : fullMessages) {
            System.out.printf("role: %s\ncontent: %s\n\n", msg.get("role"), msg.get("content"));
        }

        // 본문 구성
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", fullMessages);
        body.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> firstMessage = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) firstMessage.get("content");

            return new ChatResponseDTO(content);
        } else {
            return new ChatResponseDTO("응답 없음.");
        }
}
}

