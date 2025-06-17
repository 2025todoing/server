package hongik.Todoing.domain.chat.service;
import hongik.Todoing.domain.chat.dto.ChatResponseDTO;
import hongik.Todoing.domain.chat.dto.ChatSessionState;
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

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ChatSessionService sessionService;

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;


    private String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("prompt/prompt.txt");
            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("시스템 프롬프트 파일 로딩 실패", e);
        }
    }


    public ChatResponseDTO ask(String userId, String prompt) {

        ChatSessionState session = sessionService.get(userId);

        String url = "https://api.openai.com/v1/chat/completions";

        System.out.println("==== OPENAI API KEY ====");
        System.out.println(openaiApiKey);
        System.out.println("=========================");

        System.out.println("✅ key length = " + openaiApiKey.length());
        System.out.println("✅ prefix = " + openaiApiKey.substring(0, 10));
        System.out.println("✅ suffix = " + openaiApiKey.substring(openaiApiKey.length() - 5));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        String systemPrompt = String.format(
                loadSystemPrompt(),
                session.getCategory(),
                session.getLevel(),
                session.getStartDate(),
                session.getEndDate()
        );
        System.out.println("==== System Prompt Start ====");
        System.out.println(systemPrompt);
        System.out.println("==== System Prompt End ====");
        //시스템 프롬프트 추가
        Map<String, Object> systemMessage = Map.of(
                "role", "system",
                "content", systemPrompt);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(systemMessage, message));
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
