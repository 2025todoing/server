package hongik.Todoing.domain.todo.repository;

import hongik.Todoing.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
