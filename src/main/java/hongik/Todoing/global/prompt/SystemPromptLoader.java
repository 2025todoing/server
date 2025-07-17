package hongik.Todoing.global.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemPromptLoader {

    private final Map<String, String> sections = new HashMap<>();

    public SystemPromptLoader() {
        try (InputStream is = new ClassPathResource("prompt/prompt.txt").getInputStream()) {
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] parts = content.split("--(SYSTEM|RULE|SESSION)--");

            if (parts.length < 4)
                throw new IllegalStateException("prompt.txt 형식이 잘못되었습니다");

            sections.put("system", parts[1].trim());
            sections.put("rule", parts[2].trim());
            sections.put("session", parts[3].trim());
        } catch (Exception e) {
            throw new RuntimeException("시스템 프롬프트 파일 로딩 실패", e);
        }
    }

    public Map<String, String> getSections() {
        return sections;
    }
}
