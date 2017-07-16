package functions.adapters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.function")
public class FunctionProperties {

	private Boolean enabled = true;
	private PlatformAdapter adapter = PlatformAdapter.DISCOVERY_CLIENT;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public PlatformAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(PlatformAdapter adapter) {
		this.adapter = adapter;
	}
}
