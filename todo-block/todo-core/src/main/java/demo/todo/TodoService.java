package demo.todo;

import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventRepository;
import demo.todo.event.TodoEventService;
import demo.todo.event.TodoEventType;
import demo.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoEventRepository todoEventRepository;
    private final TodoEventService eventService;

    public TodoService(TodoRepository todoRepository,
                       TodoEventRepository todoEventRepository,
                       TodoEventService eventService) {
        this.todoRepository = todoRepository;
        this.todoEventRepository = todoEventRepository;
        this.eventService = eventService;
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

        return todoEvent;
    }

    public Todo addTodo(Long todoId, Map<String, Object> payload) {
        Todo todo = getTodo(todoId);
        Assert.notNull(todo, "Todo could not be found");

        todo = updateTodo(todo);

        TodoEvent todoEvent = new TodoEvent(TodoEventType.CREATED_EVENT, todo, payload);
        eventService.apply(todoEvent, this);

        return todo;
    }
}
