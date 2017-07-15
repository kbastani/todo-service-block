package demo.view;

import demo.event.MetricEvent;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "metrics")
public class MetricView {

    @Id
    private String id;
    private String metricName;
    private String applicationId;
    private String applicationInstanceId;
    private String applicationVersion;
    private MetricEvent metricEvent;
    private Date createdAt;
    private Date lastModified;

    public MetricView() {
    }

    public MetricView(MetricEvent metricEvent, ApplicationInstanceInfo instanceInfo, String applicationVersion) {
        this.metricEvent = metricEvent;
        this.metricName = metricEvent.getName();
        this.applicationId = instanceInfo.getAppId();
        this.applicationInstanceId = instanceInfo.getInstanceId();
        this.applicationVersion = applicationVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationInstanceId() {
        return applicationInstanceId;
    }

    public void setApplicationInstanceId(String applicationInstanceId) {
        this.applicationInstanceId = applicationInstanceId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public MetricEvent getMetricEvent() {
        return metricEvent;
    }

    public void setMetricEvent(MetricEvent metricEvent) {
        this.metricEvent = metricEvent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "MetricView{" +
                "id='" + id + '\'' +
                ", metricName='" + metricName + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationInstanceId='" + applicationInstanceId + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", metricEvent=" + metricEvent +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }
}
