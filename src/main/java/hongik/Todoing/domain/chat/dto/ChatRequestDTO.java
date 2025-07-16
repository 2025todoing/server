package hongik.Todoing.domain.chat.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRequestDTO {

    private List<Message> messages;

    @Getter
    @Setter
    public static class Message {
        private String role;
        private String content;

        // getter, setter
    }
}