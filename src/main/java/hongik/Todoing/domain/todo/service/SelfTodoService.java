package hongik.Todoing.domain.todo.service;

import hongik.Todoing.domain.todo.dto.request.TodoRequestDto;

public interface SelfTodoService {
    void createSelfTodo(TodoRequestDto request);

    void deleteSelfTodo(String userId, Long todoId);

    void completeSelfTodo(String userId, Long todoId);

}
