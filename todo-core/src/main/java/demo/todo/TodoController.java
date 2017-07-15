package demo.todo;

import demo.command.TodoCommandService;
import demo.command.TodoCommandType;
import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventRepository;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.metrics.annotation.Timed;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/v1")
@Timed
public class TodoController {

    private final TodoRepository todoRepository;
    private final TodoEventRepository eventRepository;
    private final TodoService todoService;
    private final TodoCommandService commandService;

    public TodoController(TodoRepository todoRepository, TodoEventRepository eventRepository, TodoService todoService,
                          TodoCommandService commandService) {
        this.todoRepository = todoRepository;
        this.eventRepository = eventRepository;
        this.todoService = todoService;
        this.commandService = commandService;
    }

    @RequestMapping(path = "/todos")
    public ResponseEntity getTodos() {
        return new ResponseEntity<>(todoRepository.findAll().stream()
                .map(this::getTodoResource)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(path = "/todos")
    public ResponseEntity createTodo(@RequestBody Todo todo) {
        // Apply the command
        TodoEvent todoEvent = commandService.applyCommand(TodoCommandType.CREATE, todo);

        // Return the new event resource
        return Optional.ofNullable(todoEvent)
                .map(e -> new ResponseEntity<>(getTodoEventResource(e), HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("The todo could not be found"));
    }

    @PutMapping(path = "/todos/{id}")
    public ResponseEntity updateTodo(@RequestBody Todo todo, @PathVariable Long id) {
        return Optional.ofNullable(updateTodoResource(id, todo))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Todo update failed"));
    }

    @RequestMapping(path = "/todos/{id}")
    public ResponseEntity getTodo(@PathVariable Long id) {
        return Optional.ofNullable(todoRepository.findById(id).orElse(null))
                .map(this::getTodoResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable Long id) {
        try {
            todoRepository.delete(new Todo(id));
        } catch (Exception ex) {
            throw new RuntimeException("Todo deletion failed");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(path = "/todos/{id}/events")
    public ResponseEntity getTodoEvents(@PathVariable Long id) {
        return Optional.of(getTodoEventResources(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get todo events"));
    }

    @RequestMapping(path = "/todos/{id}/events/{eventId}")
    public ResponseEntity getTodoEvent(@PathVariable Long id, @PathVariable Long eventId) {
        return Optional.of(getEventResource(eventId))
                .map(e -> new ResponseEntity<>(getTodoEventResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @RequestMapping(path = "/todos/{id}/commands")
    public ResponseEntity getCommands(@PathVariable Long id) {
        return Optional.ofNullable(getCommandsResource(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The todo could not be found"));
    }

    @RequestMapping(path = "/todos/{id}/commands/complete")
    public ResponseEntity complete(@PathVariable Long id) {
        // Apply the command
        TodoEvent todoEvent = commandService.applyCommand(TodoCommandType.COMPLETE, new Todo(id));

        // Return the new event resource
        return Optional.ofNullable(todoEvent)
                .map(e -> new ResponseEntity<>(getTodoEventResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The todo could not be found"));
    }

    @RequestMapping(path = "/todos/{id}/commands/activate")
    public ResponseEntity activate(@PathVariable Long id) {
        // Apply the command
        TodoEvent todoEvent = commandService.applyCommand(TodoCommandType.ACTIVATE, new Todo(id));

        // Return the new event resource
        return Optional.ofNullable(todoEvent)
                .map(e -> new ResponseEntity<>(getTodoEventResource(e), HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The todo could not be found"));
    }

    /**
     * Update a {@link Todo} entity for the provided identifier.
     *
     * @param id   is the unique identifier for the {@link Todo} update
     * @param todo is the entity representation containing any updated {@link Todo} fields
     * @return a hypermedia resource for the updated {@link Todo}
     */
    private Resource<Todo> updateTodoResource(Long id, Todo todo) {
        todo.setIdentity(id);
        Todo updateTodo = todoService.getTodo(id);

        if (updateTodo != null) {
            updateTodo.setStatus(todo.getStatus());
            updateTodo.setTitle(todo.getTitle());
            return getTodoResource(todoService.updateTodo(updateTodo));
        }

        return null;
    }

    private TodoEvent getEventResource(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    private List<TodoEvent> getTodoEventResources(Long id) {
        return eventRepository.findEventsByTodoId(id);
    }

    private LinkBuilder linkBuilder(String name, Long id) {
        Method method;

        try {
            method = TodoController.class.getMethod(name, Long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return linkTo(TodoController.class, method, id);
    }

    /**
     * Get a hypermedia enriched {@link Todo} entity.
     *
     * @param todo is the {@link Todo} to enrich with hypermedia links
     * @return is a hypermedia enriched resource for the supplied {@link Todo} entity
     */
    private Resource<Todo> getTodoResource(Todo todo) {
        Assert.notNull(todo, "Todo must not be null");

        if (!todo.hasLink("commands")) {
            // Add command link
            todo.add(linkBuilder("getCommands", todo.getIdentity()).withRel("commands"));
        }

        if (!todo.hasLink("events")) {
            // Add get events link
            todo.add(linkBuilder("getTodoEvents", todo.getIdentity()).withRel("events"));
        }

        return new Resource<>(todo);
    }

    private Resource<TodoEvent> getTodoEventResource(TodoEvent todoEvent) {
        Assert.notNull(todoEvent, "Todo event must not be null");

        Resource<TodoEvent> result = new Resource<>(todoEvent);

        result.add(linkTo(TodoController.class)
                .slash("todos")
                .slash(todoEvent.getTodoId())
                .withRel("todo"));

        result.add(linkTo(TodoController.class)
                .slash("todos")
                .slash(todoEvent.getTodoId())
                .slash("events")
                .slash(todoEvent.getEventId())
                .withRel("self"));

        return result;
    }

    private ResourceSupport getCommandsResource(Long id) {
        Todo todo = new Todo();
        todo.setIdentity(id);

        ResourceSupport commandResources = new ResourceSupport();

        // Add suspend command link
        commandResources.add(linkTo(TodoController.class)
                .slash("todos")
                .slash(id)
                .slash("commands")
                .slash("complete")
                .withRel("complete"));

        commandResources.add(linkTo(TodoController.class)
                .slash("todos")
                .slash(id)
                .slash("commands")
                .slash("activate")
                .withRel("activate"));

        return commandResources;
    }
}
