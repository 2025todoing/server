package hongik.Todoing.global.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SystemPromptLoader {

    public String loadPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("prompt/prompt.txt");
            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("시스템 프롬프트 파일 로딩 실패", e);
        }
    }
}