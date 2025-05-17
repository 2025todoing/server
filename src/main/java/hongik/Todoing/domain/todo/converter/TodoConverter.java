package hongik.Todoing.domain.todo.converter;

import hongik.Todoing.domain.label.domain.LabelType;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.dto.request.TodoCreateRequestDTO;
import hongik.Todoing.domain.todo.dto.response.TodoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoConverter {

    public static TodoResponseDTO toTodoDto(Todo todo) {
        LabelType labelName = null;
        if(todo.getLabel() != null) {
            labelName = todo.getLabel().getLabelName();
        }

        return new TodoResponseDTO(
                todo.getTodoId(),
                todo.getContent(),
                todo.getTodoDate(),
                todo.isCompleted(),
                todo.isAiNeeded(),
                labelName
        );
    }

    public static List<TodoResponseDTO> toTodoDtoList(List<Todo> todos) {
        return  todos.stream()
                .map(TodoConverter::toTodoDto)
                .toList();
    }

}
