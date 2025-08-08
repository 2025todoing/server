package hongik.Todoing.domain.chat.event;

import hongik.Todoing.domain.chat.dto.request.ChatRequestDTO;

import java.util.List;

public record GptRequestEvent(String userId, List<ChatRequestDTO.Message> messages) {
}