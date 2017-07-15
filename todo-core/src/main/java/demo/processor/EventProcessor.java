package demo.processor;

import demo.todo.event.TodoEvent;
import demo.todo.event.TodoEventType;
import org.apache.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
@EnableBinding(Sink.class)
public class EventProcessor {

    private final Logger log = Logger.getLogger(this.getClass());

    @StreamListener(value = Sink.INPUT)
    public void handle(Message<TodoEvent> message) {
        TodoEvent todoEvent = message.getPayload();
        log.info("Received new event: " + "{ todoId " + todoEvent.getTodoId() + " -> " +
                todoEvent.getType() + " }");

        if (todoEvent.getType() == TodoEventType.CREATED_EVENT) {
        }
    }
}
