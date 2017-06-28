package demo.event;

import demo.todo.TodoService;
import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class EventController {

    private final TodoService todoService;
    private final TodoEventService todoEventService;

    public EventController(TodoService todoService, TodoEventService todoEventService) {
        this.todoService = todoService;
        this.todoEventService = todoEventService;
    }

    @PostMapping(path = "/events")
    public ResponseEntity handleEvent(@RequestBody TodoEvent event) {
        return Optional.ofNullable(todoEventService.apply(event, todoService))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Apply event failed"));
    }
}
