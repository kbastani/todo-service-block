package demo.todo.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoEventRepository extends JpaRepository<TodoEvent, Long> {
    List<TodoEvent> findEventsByTodoId(@Param("todoId") Long todoId);
}
