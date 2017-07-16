package demo.command;

import demo.event.MetricEvent;
import demo.function.FunctionService;
import demo.todo.Todo;
import demo.todo.TodoService;
import demo.todo.event.TodoEvent;
import demo.view.MetricView;
import functions.adapters.FunctionInvoker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;

@Service
public class TodoCommandService {

	private final FunctionInvoker functionInvoker;
	private final TodoService todoService;

	public TodoCommandService(FunctionInvoker functionInvoker, TodoService todoService) {
		this.functionInvoker = functionInvoker;
		this.todoService = todoService;
	}

	@Transactional
	public TodoEvent applyCommand(TodoCommandType commandType, Todo todo) {
		TodoEvent todoEvent = null;

		if (todo != null && todo.getIdentity() != null)
			todo = todoService.getTodo(todo.getIdentity());

		Assert.notNull(todo, "A todo for that ID does not exist");

		// Map the event type to the corresponding command handler
		switch (commandType) {
			case CREATE:
				todoEvent = TodoCommands.createTodo(todo);
				break;
			case ACTIVATE:
				todoEvent = TodoCommands.activateTodo(todo);
				break;
			case COMPLETE:
				todoEvent = TodoCommands.completeTodo(todo);
				break;
		}

		Assert.notNull(todoEvent, "The command handler did not generate a todo event");

		// Apply command event
		todo = todoService.updateTodo(todo);

		// Add the event and reset the transient payload
		todoEvent = todoService.addTodoEvent(todo.getIdentity(), todoEvent);
		todoEvent.setPayload(new HashMap<>());
		todoEvent.setEntity(todo);

		return todoEvent;
	}

	public MetricView applyMetricEvent(MetricEvent metric) {
		return functionInvoker.getFunctionService(FunctionService.class)
			.metricsFunction(metric);
	}
}
