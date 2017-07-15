package amazon.aws;

public interface FunctionInvoker {

	<T> T getFunctionService(Class<T> functionService);
}
