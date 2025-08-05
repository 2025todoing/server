package hongik.Todoing.domain.todo.converter;

import hongik.Todoing.domain.label.domain.LabelType;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.dto.response.TodoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoConverter {

    private final LabelRepository labelRepository;

    public TodoResponseDTO toTodoDto(Todo todo) {
        LabelType labelName = labelRepository.findByLabelId(todo.getLabelId()).getLabelName();

        return new TodoResponseDTO(
                todo.getTodoId(),
                todo.getContent(),
                todo.getTodoDate(),
                todo.isCompleted(),
                todo.isAiNeeded(),
                labelName
        );
    }

    public List<TodoResponseDTO> toTodoDtoList(List<Todo> todos) {
        return  todos.stream()
                .map(this::toTodoDto)
                .toList();
    }

}
