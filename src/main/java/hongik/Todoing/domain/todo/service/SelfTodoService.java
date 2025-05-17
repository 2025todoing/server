package hongik.Todoing.domain.todo.service;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todo.converter.TodoConverter;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.dto.request.TodoCreateRequestDTO;
import hongik.Todoing.domain.todo.dto.request.TodoUpdateRequestDTO;
import hongik.Todoing.domain.todo.dto.response.TodoResponseDTO;
import hongik.Todoing.domain.todo.repository.TodoRepository;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SelfTodoService {

    private final LabelRepository labelRepository;
    private final TodoRepository todoRepository;


    public void createTodo(Member member, TodoCreateRequestDTO request) {
        Label label = labelRepository.findByLabelId(request.labelId());

        Todo todo = Todo.builder()
                .member(member)
                .content(request.content())
                .todoDate(request.todoDate())
                .label(label)
                .isAiNeeded(false)
                .isCompleted(false)
                .build();

        todoRepository.save(todo);
    }

    public List<TodoResponseDTO> getTodosByDate(Member member, LocalDate date) {
        List<Todo> todos = todoRepository.findByMemberAndTodoDate(member, date);
        return TodoConverter.toTodoDtoList(todos);
    }

    public void deleteTodo(Member member, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TODO_NOT_FOUND);
        }

        todoRepository.delete(todo);
    }

    public void toggleTodo(Member member, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TODO_NOT_FOUND);
        }

        todo.updateComplete(!todo.isCompleted());
    }

    public void updateTodo(Member member, Long todoId, TodoUpdateRequestDTO requestDTO) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TODO_NOT_FOUND);
        }

        Label label = labelRepository.findByLabelId(requestDTO.labelId());

        todo.updateTodo(requestDTO.content(), requestDTO.date(), label);
    }

}
