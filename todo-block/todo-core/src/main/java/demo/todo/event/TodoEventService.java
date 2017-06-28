package demo.todo.event;

import demo.event.EventService;
import demo.todo.TodoService;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoEventService {

    private final EventService eventService;
    private final Source source;

    public TodoEventService(EventService eventService, Source source) {
        this.eventService = eventService;
        this.source = source;
    }

    @Transactional
    public TodoEvent apply(TodoEvent todoEvent, TodoService todoService) {
        todoEvent = eventService.apply(todoEvent, todoService);

        // Send the event to the event stream to update query models
        sendEvent(todoEvent);

        return todoEvent;
    }

    private void sendEvent(TodoEvent todoEvent) {
        source.output().send(MessageBuilder.withPayload(todoEvent).build());
    }
}
