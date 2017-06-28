package demo.command;

import org.springframework.stereotype.Service;

@Service
public class TodoCommands {

    private final CreateTodo createTodo;

    public TodoCommands(CreateTodo createTodo) {
        this.createTodo = createTodo;
    }

    public CreateTodo getCreateTodo() {
        return createTodo;
    }
}
