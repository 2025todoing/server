package hongik.Todoing.domain.todo.repository;

import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByMember(Member member);

    Optional<Todo> findByTodoId(Long todoId);

    // todo 볼 때 라벨별로 봐야 함.
    @Query("""
        SELECT t
        FROM Todo t
        WHERE t.member = :member AND t.todoDate = :date
        ORDER BY t.label.labelName ASC
        """)
    List<Todo> findByMemberAndTodoDate(@Param("member") Member member,@Param("date") LocalDate date);
}