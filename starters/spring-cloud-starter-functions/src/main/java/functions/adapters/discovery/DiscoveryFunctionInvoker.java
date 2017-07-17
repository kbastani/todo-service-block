package functions.adapters.discovery;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import functions.adapters.FunctionInvoker;
import functions.adapters.FunctionRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A {@link FunctionInvoker} implementation for using a {@link DiscoveryClient} to lookup and invoke functional
 * services.
 *
 * @author Kenny Bastani
 */
@Component
public class DiscoveryFunctionInvoker extends FunctionInvoker {

	private RestTemplate restTemplate;

	/**
	 * Create a {@link DiscoveryFunctionInvoker}.
	 *
	 * @param restTemplate is a {@link LoadBalanced} {@link RestTemplate}
	 */
	DiscoveryFunctionInvoker(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Gets an instance of a {@link FunctionRegistry} that routes service requests to a discovery service provider.
	 *
	 * @param registry is a {@link FunctionRegistry} that describes functional service invocations
	 * @param <T>      is the type of {@link FunctionRegistry} to retrieve
	 * @return an instance of a {@link FunctionRegistry} that is used to lookup and invoke functions on a
	 * target platform
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected <T extends FunctionRegistry> T getRegistryInstance(Class<T> registry) {
		if (!registryContainer.containsKey(registry.getName())) {
			registryContainer.put(registry.getName(), ProxyFactory.getProxy(registry, invoke()));
		}
		return (T) registryContainer.get(registry.getName());
	}

	/**
	 * Invokes a functional service using the method name described in a {@link LambdaFunction} that annotates
	 * a method on a {@link FunctionRegistry}. The invocation will make an HTTP POST request to the service instance
	 * and return the response.
	 *
	 * @return a {@link MethodInterceptor} that invokes a service instance
	 */
	private MethodInterceptor invoke() {
		return methodInvocation -> {
			// Get return type
			Class<?> returnType = methodInvocation.getMethod().getReturnType();
			LambdaFunction lambdaFunction = methodInvocation.getMethod().getAnnotation(LambdaFunction.class);
			Object result = null;

			if (lambdaFunction != null) {
				Object param;

				if (methodInvocation.getArguments().length == 1) {
					// Post request to function
					param = methodInvocation.getArguments()[0];

					result = restTemplate
						.postForObject(String.format("http://%s/function", lambdaFunction.functionName()),
							param, returnType);
				}
			}

			return result;
		};
	}
}
