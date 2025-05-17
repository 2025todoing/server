package hongik.Todoing.domain.todo.dto.request;

import java.time.LocalDate;

public record TodoUpdateRequestDTO(
        String content,
        LocalDate date,
        Long labelId
) {
}
