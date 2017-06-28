package demo.todo.repository;

import demo.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Todo findTodoByTitle(@Param("title") String title);
}
