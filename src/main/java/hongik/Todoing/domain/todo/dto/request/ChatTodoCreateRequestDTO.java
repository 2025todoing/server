package hongik.Todoing.domain.todo.dto.request;


import lombok.Getter;

import java.util.List;

@Getter
public class ChatTodoCreateRequestDTO {
    private String mainQuest;
    private List<SubQuest> subQuests;

    @Getter
    public static class SubQuest {
        private String date;
        private String task;

    }
}
