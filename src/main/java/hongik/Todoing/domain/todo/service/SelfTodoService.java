package hongik.Todoing.domain.todo.service;

import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.repository.MemberRepository;
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

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final LabelRepository labelRepository;

    @Transactional
    public void createTodo(Long memberId, String content, LocalDate todoDate, Label labelId, boolean isAiNeeded) {
        Todo todo = Todo.builder()
                .memberId(memberId)
                .content(content)
                .todoDate(todoDate)
                .label(labelId)
                .isAiNeeded(isAiNeeded)
                .isCompleted(false)
                .build();

        todoRepository.save(todo);
    }

    public List<TodoResponseDTO> getTodosByDate(Long memberId, LocalDate date) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<Todo> todos = todoRepository.findByMemberIdAndTodoDate(member.getId(), date);
        return TodoConverter.toTodoDtoList(todos);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        Member member = memberRepository.findById(memberId).orElseThrow();
        Member member1 = memberRepository.findById(todo.getMemberId()).orElseThrow();

        if(!member1.getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        todoRepository.delete(todo);
    }

    @Transactional
    public void toggleTodo(Long memberId, Long todoId) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        Member member1 = memberRepository.findById(todo.getMemberId()).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        if(!member1.getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }


        todo.updateComplete(!todo.isCompleted());
    }

    @Transactional
    public void updateTodo(Long memberId, Long todoId, TodoUpdateRequestDTO requestDTO) {
        Todo todo = todoRepository.findByTodoId(todoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TODO_NOT_FOUND));

        Member member1 = memberRepository.findById(todo.getMemberId()).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        if(!member1.getEmail().equals(member.getEmail())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Label label = labelRepository.findByLabelName(requestDTO.labelType())
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND));

        todo.updateTodo(requestDTO.content(), requestDTO.date(), label);
    }

}