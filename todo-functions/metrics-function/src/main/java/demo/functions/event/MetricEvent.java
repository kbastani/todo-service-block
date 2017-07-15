package demo.functions.event;

import org.springframework.boot.actuate.metrics.Metric;

/**
 * Serializable wrapper for Spring Boot Actuator {@link Metric} events.
 *
 * @author Kenny Bastani
 */
public class MetricEvent<T extends Number> extends Metric<T> {

    private InstanceDetails instanceDetails;

    public MetricEvent() {
        super("", null);
    }

    public MetricEvent(Metric<T> metric) {
        super(metric.getName(), metric.getValue(), metric.getTimestamp());
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
                "instanceDetails=" + instanceDetails +
                '}';
    }
}
