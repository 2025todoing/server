package hongik.Todoing.domain.todo.dto.request;

import hongik.Todoing.domain.label.domain.LabelType;

import java.time.LocalDate;

public record TodoUpdateRequestDTO(
        String content,
        LocalDate date,
        LabelType labelType
) {
}
