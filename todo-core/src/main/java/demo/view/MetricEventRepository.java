package demo.view;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface MetricEventRepository extends ReactiveMongoRepository<MetricView, String> {

    Flux<MetricView> findByMetricName(@Param("metricName") String metricName);
    Flux<MetricView> findByApplicationId(@Param("applicationId") String applicationId, Pageable pageable);
}
