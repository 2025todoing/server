package hongik.Todoing.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoRequestDto {
    private LocalDate date;
    private String content;
    private Long labelId;
}
