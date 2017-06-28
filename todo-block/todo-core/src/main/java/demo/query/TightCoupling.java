package demo.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.config.AwsLambdaConfig;
import demo.function.FunctionService;
import demo.function.LambdaResponse;
import demo.todo.Todo;
import demo.todo.event.TodoEvent;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TightCoupling {

    private final Logger log = Logger.getLogger(TightCoupling.class);
    private final FunctionService functionService;
    private final QueryEventRepository eventRepository;

    public TightCoupling(AwsLambdaConfig.FunctionInvoker functionService,
                         QueryEventRepository eventRepository) {
        this.functionService = functionService.getFunctionService();
        this.eventRepository = eventRepository;
    }

    public LambdaResponse<Map<String, Object>> apply(TodoEvent event) {
        try {
            Map<String, Object> result =
                    functionService.metricsFunction(getTodoEventMap(event, event.getEntity()));

            log.info(result);

            ObjectMapper objectMapper = new ObjectMapper();
            // Check for new tight coupling events
            List<TightCouplingEvent> events =
                    Stream.of(objectMapper.readValue(objectMapper.writeValueAsString(result.get("events")),
                            TightCouplingEvent[].class)).collect(Collectors.toList());

            log.info(events);

            eventRepository.saveAll(events).subscribe();

            return new LambdaResponse<>(result);
        } catch (Exception ex) {
            log.error("Error invoking AWS Lambda function", ex);
            throw new RuntimeException("Error invoking function", ex);
        }
    }

    private Map<String, Object> getTodoEventMap(TodoEvent event, Todo todo) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("todoEvent", event);
        eventMap.put("todo", new Todo(event.getTodoId()));
        return eventMap;
    }
}
