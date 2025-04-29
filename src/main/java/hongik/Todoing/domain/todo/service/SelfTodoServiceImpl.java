package hongik.Todoing.domain.todo.service;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.dto.request.TodoRequestDto;
import hongik.Todoing.domain.todo.repository.TodoRepository;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelfTodoServiceImpl implements SelfTodoService{

    private final LabelRepository labelRepository;
    private final TodoRepository todoRepository;
    @Override
    public void createSelfTodo(TodoRequestDto request) {
        Label label = labelRepository.findByLabelId(request.getLabelId());

        Todo todo = Todo.builder()
                .todoDate(request.getDate())
                .content(request.getContent())
                .label(label)
                .build();

        // Save the todo to the database (assuming you have a repository for Todo)
        todoRepository.save(todo);
    }

    @Override
    public void deleteSelfTodo(String userId, Long todoId) {
        // Find the todo by ID and delete it
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GeneralException((ErrorStatus.BAD_REQUEST)));

        todoRepository.delete(todo);
    }

    @Override
    public void completeSelfTodo(String userId, Long todoId) {
        // Find the todo by ID and mark it as completed
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GeneralException((ErrorStatus.BAD_REQUEST)));

        todo.setCompleted(true);
        todoRepository.save(todo);
    }
}
