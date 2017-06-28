package demo.todo.controller;

import demo.todo.Todo;
import demo.todo.TodoService;
import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventRepository;
import demo.todo.event.TodoEventService;
import demo.todo.event.TodoEventType;
import demo.todo.repository.TodoRepository;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/v1")
public class TodoController {

    private final TodoRepository todoRepository;
    private final TodoEventRepository eventRepository;
    private final TodoService todoService;
    private final TodoEventService todoEventService;

    public TodoController(TodoRepository todoRepository, TodoEventRepository eventRepository, TodoService todoService,
                          TodoEventService todoEventService) {
        this.todoRepository = todoRepository;
        this.eventRepository = eventRepository;
        this.todoService = todoService;
        this.todoEventService = todoEventService;
    }

    @RequestMapping(path = "/todos")
    public ResponseEntity getTodos() {
        return new ResponseEntity<>(todoRepository.findAll().stream()
                .map(this::getTodoResource)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping(path = "/todos")
    public ResponseEntity createTodo(@RequestBody Todo todo) {
        return Optional.ofNullable(createTodoResource(todo))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Todo creation failed"));
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
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @PostMapping(path = "/todos/{id}/events")
    public ResponseEntity appendTodoEvent(@PathVariable Long id, @RequestBody TodoEvent event) {
        return Optional.ofNullable(appendEventResource(id, event))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Append todo event failed"));
    }

    @RequestMapping(path = "/todos/{id}/commands")
    public ResponseEntity getCommands(@PathVariable Long id) {
        return Optional.ofNullable(getCommandsResource(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The todo could not be found"));
    }

    /**
     * Creates a new {@link Todo} entity and persists the result to the repository.
     *
     * @param todo is the {@link Todo} model used to create a new todo
     * @return a hypermedia resource for the newly created {@link Todo}
     */
    private Resource<Todo> createTodoResource(Todo todo) {
        Assert.notNull(todo, "Todo body must not be null");

        todo = todoService.updateTodo(todo);
        todo = todoEventService.apply(new TodoEvent(TodoEventType.CREATED_EVENT, todo), todoService).getEntity();

        return getTodoResource(todo);
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

        if(updateTodo != null) {
            updateTodo.setStatus(todo.getStatus());
            updateTodo.setTitle(todo.getTitle());
            return getTodoResource(todoService.updateTodo(updateTodo));
        }

        return null;
    }

    /**
     * Appends an {@link TodoEvent} domain event to the event log of the {@link Todo}
     * aggregate with the specified todoId.
     *
     * @param todoId is the unique identifier for the {@link Todo}
     * @param event  is the {@link TodoEvent} that attempts to alter the state of the {@link Todo}
     * @return a hypermedia resource for the newly appended {@link TodoEvent}
     */
    private Resource<TodoEvent> appendEventResource(Long todoId, TodoEvent event) {
        Assert.notNull(event, "Event body must be provided");

        Todo todo = todoRepository.findById(todoId).get();
        Assert.notNull(todo, "Todo could not be found");

        event.setTodoId(todo.getIdentity());
        todoEventService.apply(event, todoService);

        return new Resource<>(event,
                linkTo(TodoController.class)
                        .slash("todos")
                        .slash(todoId)
                        .slash("events")
                        .slash(event.getEventId())
                        .withSelfRel(),
                linkTo(TodoController.class)
                        .slash("todos")
                        .slash(todoId)
                        .withRel("todo")
        );
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

    private ResourceSupport getCommandsResource(Long id) {
        Todo todo = new Todo();
        todo.setIdentity(id);

        CommandResources commandResources = new CommandResources();

        // Add suspend command link
        commandResources.add(linkTo(TodoController.class)
                .slash("todos")
                .slash(id)
                .slash("commands")
                .slash("complete")
                .withRel("complete"));

        return new Resource<>(commandResources);
    }

    public static class CommandResources extends ResourceSupport {
    }
}
