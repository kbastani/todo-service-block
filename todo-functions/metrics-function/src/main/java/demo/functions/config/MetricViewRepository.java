package demo.functions.config;

import demo.functions.view.MetricView;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetricViewRepository extends MongoRepository<MetricView, String> {
}
