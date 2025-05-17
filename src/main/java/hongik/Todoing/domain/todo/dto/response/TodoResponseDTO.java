package hongik.Todoing.domain.todo.dto.response;

import hongik.Todoing.domain.label.domain.LabelType;

import java.time.LocalDate;

public record TodoResponseDTO (
        Long todoId,
        String content,
        LocalDate todoDate,
        boolean isCompleted,
        boolean isAiNeeded,
        LabelType labelName
) {

}
