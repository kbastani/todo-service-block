package functions.adapters;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public abstract class FunctionInvoker implements ApplicationContextAware {

	protected final Map<String, FunctionRegistry> registryContainer = new HashMap<>();
	private ApplicationContext context;

	public <T extends FunctionRegistry> T getFunctionService(Class<T> registry) {
		if (!registryContainer.containsKey(registry.getName()))
			registryContainer.put(registry.getName(), getRegistryInstance(registry));

		return (T) registryContainer.get(registry.getName());
	}

	protected abstract <T extends FunctionRegistry> T getRegistryInstance(Class<T> registry);

	@Override
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
		setProperties();
	}

	public void setProperties() {
	}
}
