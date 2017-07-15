package demo.functions.event;

public class InstanceDetails {

    private String instanceId;
    private String appId;
    private String version;

    public InstanceDetails() {
    }

    public InstanceDetails(String instanceId, String appId, String version) {
        this.instanceId = instanceId;
        this.appId = appId;
        this.version = version;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return "InstanceDetails{" +
                "instanceId='" + instanceId + '\'' +
                ", appId='" + appId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
