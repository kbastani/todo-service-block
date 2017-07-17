package functions.adapters;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link FunctionInvoker} is the base abstraction for invoking functional services on a target platform.
 *
 * @author Kenny Bastani
 * @see FunctionPlatformProvider
 */
public abstract class FunctionInvoker {

	protected final Map<String, FunctionRegistry> registryContainer = new HashMap<>();

	/**
	 * Gets an instance of a {@link FunctionRegistry} that routes service requests to a functional service
	 * provider.
	 *
	 * @param registry is a {@link FunctionRegistry} that describes functional service invocations
	 * @param <T>      is the type of {@link FunctionRegistry} to retrieve
	 * @return an instance of a {@link FunctionRegistry} that is used to lookup and invoke functions on a
	 * target platform
	 */
	@SuppressWarnings("unchecked")
	public <T extends FunctionRegistry> T getRegistry(Class<T> registry) {
		if (!registryContainer.containsKey(registry.getName()))
			registryContainer.put(registry.getName(), getRegistryInstance(registry));

		return (T) registryContainer.get(registry.getName());
	}

	/**
	 * An abstract method that is implemented for each functional service adapter, turning a {@link FunctionRegistry}
	 * interface into a proxied service component.
	 *
	 * @param registry is an interface describing the functional services to lookup and invoke
	 * @param <T>      is the type of {@link FunctionRegistry} to create an instance of
	 * @return an instance of a {@link FunctionRegistry} that is used to lookup and invoke functions on a
	 * target platform
	 */
	protected abstract <T extends FunctionRegistry> T getRegistryInstance(Class<T> registry);
}
