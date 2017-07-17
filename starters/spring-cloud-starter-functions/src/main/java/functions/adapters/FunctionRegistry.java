package functions.adapters;

/**
 * A {@link FunctionRegistry} is a base interface abstraction for looking up and invoking functional services
 * on a variety of different platforms. A function registry will define a set of methods that will be
 * proxied at runtime. A {@link FunctionRegistry} is similar to a service registry, but for serverless
 * functions. Depending on the configured {@link FunctionPlatformProvider}, the proxied function registry methods
 * will route requests to a target platform.
 *
 * @author Kenny Bastani
 * @see FunctionPlatformProvider
 * @see FunctionInvoker
 */
public interface FunctionRegistry {
}
