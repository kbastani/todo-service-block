package functions.adapters.discovery;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import functions.adapters.FunctionInvoker;
import functions.adapters.FunctionRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscoveryFunctionInvoker extends FunctionInvoker {

	private ApplicationContext applicationContext;
	private RestTemplate restTemplate;

	public DiscoveryFunctionInvoker(ApplicationContext applicationContext, RestTemplate restTemplate) {
		this.applicationContext = applicationContext;
		this.restTemplate = restTemplate;
	}

	@Override
	public void setProperties() {
	}

	@Override
	protected <T extends FunctionRegistry> T getRegistryInstance(Class<T> registry) {
		if (!registryContainer.containsKey(registry.getName())) {
			registryContainer.put(registry.getName(), ProxyFactory.getProxy(registry, invoke()));
		}
		return (T) registryContainer.get(registry.getName());
	}

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
