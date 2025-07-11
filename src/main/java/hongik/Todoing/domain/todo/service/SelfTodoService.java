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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelfTodoService {

    private final LabelRepository labelRepository;
    private final TodoRepository todoRepository;


    @Transactional
    public void createTodo(Member member, TodoCreateRequestDTO request) {
        Label label = labelRepository.findByLabelName(request.labelType())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND));

        Todo todo = Todo.builder()
                .member(member)
                .content(request.content())
                .todoDate(request.todoDate())
                .label(label)
                .isAiNeeded(request.isAiNeeded())
                .isCompleted(false)
                .build();

        todoRepository.save(todo);
    }

    public List<TodoResponseDTO> getTodosByDate(Member member, LocalDate date) {
        List<Todo> todos = todoRepository.findByMemberAndTodoDate(member, date);
        return TodoConverter.toTodoDtoList(todos);
    }

    @Transactional
    public void deleteTodo(Member member, Long todoId) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        todoRepository.delete(todo);
    }

    @Transactional
    public void toggleTodo(Member member, Long todoId) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }


        todo.updateComplete(!todo.isCompleted());
    }

    @Transactional
    public void updateTodo(Member member, Long todoId, TodoUpdateRequestDTO requestDTO) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        if(!todo.getMember().getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Label label = labelRepository.findByLabelName(requestDTO.labelType())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND));

        todo.updateTodo(requestDTO.content(), requestDTO.date(), label);
    }

}