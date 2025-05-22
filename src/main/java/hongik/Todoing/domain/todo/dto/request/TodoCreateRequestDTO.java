package hongik.Todoing.domain.todo.dto.request;

import hongik.Todoing.domain.label.domain.LabelType;

import java.time.LocalDate;

public record TodoCreateRequestDTO(
        String content,
        LocalDate todoDate,
        LabelType labelType
) {}
