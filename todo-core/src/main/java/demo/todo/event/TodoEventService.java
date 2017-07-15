package demo.todo.event;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoEventService {

    private final Source source;

    public TodoEventService(Source source) {
        this.source = source;
    }

    @Transactional
    public TodoEvent apply(TodoEvent todoEvent) {
        // Send the event to the event stream to update query models
        sendEvent(todoEvent);

        return todoEvent;
    }

    private void sendEvent(TodoEvent todoEvent) {
        source.output().send(MessageBuilder.withPayload(todoEvent).build());
    }
}
