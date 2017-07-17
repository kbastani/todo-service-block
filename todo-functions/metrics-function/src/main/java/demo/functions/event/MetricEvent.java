package demo.functions.event;

import org.springframework.boot.actuate.metrics.Metric;

import java.util.Date;

/**
 * Serializable wrapper for Spring Boot Actuator {@link Metric} events.
 *
 * @author Kenny Bastani
 */
public class MetricEvent<T extends Number> {

    private String name;
    private T value;
    private Date timestamp;

    private InstanceDetails instanceDetails;

    public MetricEvent() {
    }

    public MetricEvent(Metric<T> metric, InstanceDetails instanceDetails) {
        this.name = metric.getName();
        this.value = metric.getValue();
        this.timestamp = metric.getTimestamp();
        this.instanceDetails = instanceDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public InstanceDetails getInstanceDetails() {
        return instanceDetails;
    }

    public void setInstanceDetails(InstanceDetails instanceDetails) {
        this.instanceDetails = instanceDetails;
    }

    @Override
    public String toString() {
        return "MetricEvent{" +
            "name='" + name + '\'' +
            ", value=" + value +
            ", timestamp=" + timestamp +
            ", instanceDetails=" + instanceDetails +
            '}';
    }
}
