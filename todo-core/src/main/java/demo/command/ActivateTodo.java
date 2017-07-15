package demo.command;

import demo.todo.Todo;
import demo.todo.TodoStatus;
import demo.todo.event.TodoEvent;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


public class ActivateTodo {

    private final Logger log = Logger.getLogger(CreateTodo.class);

    public Todo apply(TodoEvent todoEvent) {
        log.info("Todo Activated: " + todoEvent);
        Assert.isTrue((todoEvent.getEntity().getStatus() == TodoStatus.ACTIVE ||
                todoEvent.getEntity().getStatus() == null), "Todo is in an invalid state");
        Todo todo = todoEvent.getEntity();
        todo.setStatus(TodoStatus.ACTIVE);
        return todo;
    }

}
