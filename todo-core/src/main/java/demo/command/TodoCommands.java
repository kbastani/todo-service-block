package demo.command;

import demo.todo.Todo;
import demo.todo.TodoStatus;
import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventType;
import org.springframework.util.Assert;

public class TodoCommands {

    public static TodoEvent createTodo(Todo todo) {
        Assert.isTrue(todo.getStatus() != TodoStatus.CREATED,
                "An invalid status was provided. Valid statuses: CREATED or null");
        todo.setStatus(TodoStatus.CREATED);
        return new TodoEvent(TodoEventType.CREATED_EVENT, todo);
    }

    public static TodoEvent activateTodo(Todo todo) {
        Assert.isTrue(todo.getStatus() != TodoStatus.ACTIVE, "The Todo is already active");
        todo.setStatus(TodoStatus.ACTIVE);
        return new TodoEvent(TodoEventType.ACTIVATED_EVENT, todo);
    }

    public static TodoEvent completeTodo(Todo todo) {
        Assert.isTrue(todo.getStatus() != TodoStatus.COMPLETED, "The Todo is already complete");
        todo.setStatus(TodoStatus.COMPLETED);
        return new TodoEvent(TodoEventType.COMPLETED_EVENT, todo);
    }
}
