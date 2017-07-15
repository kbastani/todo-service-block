package demo.todo;

import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventRepository;
import demo.todo.event.TodoEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoEventRepository todoEventRepository;
    private final TodoEventService todoEventService;

    public TodoService(TodoRepository todoRepository, TodoEventRepository todoEventRepository,
                       TodoEventService todoEventService) {
        this.todoRepository = todoRepository;
        this.todoEventRepository = todoEventRepository;
        this.todoEventService = todoEventService;
    }

    public Todo getTodo(Long todoId) {
        return todoRepository.findById(todoId).get();
    }

    public Todo updateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public TodoEvent addTodoEvent(Long todoId, TodoEvent todoEvent) {
        Todo todo = getTodo(todoId);
        Assert.notNull(todo, "Todo could not be found");

        todoEvent.setTodoId(todoId);
        todoEvent.setEntity(todo);
        todoEvent = todoEventRepository.saveAndFlush(todoEvent);
        todo.getEvents().add(todoEvent);
        updateTodo(todo);

        todoEventService.apply(todoEvent);

        return todoEvent;
    }
}
