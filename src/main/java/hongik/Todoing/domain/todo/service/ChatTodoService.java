package hongik.Todoing.domain.todo.service;


import hongik.Todoing.domain.chat.dto.ChatSessionState;
import hongik.Todoing.domain.label.domain.Label;
import hongik.Todoing.domain.label.domain.LabelType;
import hongik.Todoing.domain.label.repository.LabelRepository;
import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todo.domain.Todo;
import hongik.Todoing.domain.todo.dto.request.ChatTodoCreateRequestDTO;
import hongik.Todoing.domain.todo.repository.TodoRepository;
import hongik.Todoing.global.apiPayload.code.status.ErrorStatus;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatTodoService {

    private final TodoRepository todoRepository;
    private final LabelRepository labelRepository;

    @Transactional
    public void createTodo(Member member, ChatTodoCreateRequestDTO requestDTO, ChatSessionState sessionState){
        Label label = labelRepository.findByLabelName(LabelType.valueOf(sessionState.getCategory().toUpperCase()))
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND));

        for (ChatTodoCreateRequestDTO.SubQuest subQuest : requestDTO.getSubQuests()) {
            Todo todo = Todo.builder()
                    .member(member)
                    .content(subQuest.getTask())
                    .todoDate(LocalDate.parse(subQuest.getDate()))
                    .label(label)
                    .isAiNeeded(false)
                    .isCompleted(false)
                    .build();

            todoRepository.save(todo);
        }

    }

}
