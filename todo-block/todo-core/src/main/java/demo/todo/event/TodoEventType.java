package demo.todo.event;

import demo.todo.Todo;
import demo.todo.TodoStatus;

/**
 * The {@link TodoEventType} represents a collection of possible events that describe
 * state transitions of {@link TodoStatus} on the {@link Todo} aggregate.
 *
 * @author kbastani
 */
public enum TodoEventType {
    CREATED_EVENT
}
