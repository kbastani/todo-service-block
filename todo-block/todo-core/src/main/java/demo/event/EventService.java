package demo.event;

import demo.command.TodoCommands;
import demo.todo.Todo;
import demo.todo.TodoService;
import demo.todo.event.TodoEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

@Service
public class EventService {

    final private TodoCommands todoActions;

    public EventService(TodoCommands todoActions) {
        this.todoActions = todoActions;
    }

    @Transactional
    public TodoEvent apply(TodoEvent todoEvent, TodoService todoService) {
        Todo todo = todoService.getTodo(todoEvent.getTodoId());

        // Cache payload before handling the event
        Map<String, Object> payload = todoEvent.getPayload();

        if (todo == null)
            todo = todoEvent.getEntity();

        Assert.notNull(todo, "A todo for that ID does not exist");

        // Map the event type to the corresponding command handler
        switch (todoEvent.getType()) {
            case CREATED_EVENT:
                todo = todoActions.getCreateTodo().apply(todoEvent);
                break;
        }

        // Apply command updates
        todo = todoService.updateTodo(todo);

        // Add the event and reset the transient payload
        todoEvent = todoService.addTodoEvent(todo.getIdentity(), todoEvent);
        todoEvent.setPayload(payload);
        todoEvent.setEntity(todo);

        return todoEvent;
    }
}
