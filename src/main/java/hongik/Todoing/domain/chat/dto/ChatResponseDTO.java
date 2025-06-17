package hongik.Todoing.domain.chat.dto;

import hongik.Todoing.global.apiPayload.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseDTO {

    public ChatResponseDTO() {}

    public ChatResponseDTO(String prompt) {
        this.prompt = prompt;

    }
    private String prompt;
}
