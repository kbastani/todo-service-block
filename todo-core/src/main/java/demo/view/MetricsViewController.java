package demo.view;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;

@RestController
@RequestMapping("/v1")
public class MetricsViewController {

    private final MetricEventRepository metricEventRepository;

    public MetricsViewController(MetricEventRepository metricEventRepository) {
        this.metricEventRepository = metricEventRepository;
    }

    @GetMapping(value = "/apps/{appId}/metrics", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MetricView>> streamMetricsByAppId(@PathVariable String appId,
                                                                  HttpServletRequest request) {
        // Stream the events from MongoDB
        Flux<MetricView> events = metricEventRepository
                .findByApplicationId(appId, PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "createdAt")))
                .sort(Comparator.comparing(MetricView::getCreatedAt));

        // Check if this is an SSE reconnection from a client
        String lastEventId = request.getHeader("Last-Event-Id");

        // On SSE client reconnect, skip ahead in the stream to play back only new events
        if (lastEventId != null)
            events = events.skipUntil(e -> e.getCreatedAt().getTime() > Long.parseLong(lastEventId)).skip(1);

        // Subscribe to the tailing events from the reactive repository query
        return events.map(s -> ServerSentEvent.builder(s)
                .event(String.valueOf(s.getCreatedAt().getTime()))
                .id(String.valueOf(s.getCreatedAt().getTime()))
                .build());
    }

    @GetMapping(value = "/metrics/{metricName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MetricView>> streamMetricByName(@PathVariable String metricName,
                                                                HttpServletRequest request) {

        // Stream the events from MongoDB
        Flux<MetricView> events = metricEventRepository.findByMetricName(metricName);

        // Check if this is an SSE reconnection from a client
        String lastEventId = request.getHeader("Last-Event-Id");

        // On SSE client reconnect, skip ahead in the stream to play back only new events
        if (lastEventId != null)
            events = events.skipUntil(e -> e.getId().equals(lastEventId)).skip(1);

        // Subscribe to the tailing events from the reactive repository query
        return events.map(s -> ServerSentEvent.builder(s)
                .event(String.valueOf(s.getCreatedAt().getTime()))
                .id(s.getId())
                .build());
    }
}
