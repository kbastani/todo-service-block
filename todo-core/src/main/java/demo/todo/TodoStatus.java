package demo.todo;

import demo.todo.event.TodoEvent;

/**
 * The {@link TodoStatus} describes the state of an {@link Todo}.
 * The aggregate state of a {@link Todo} is sourced from attached domain
 * events in the form of {@link TodoEvent}.
 *
 * @author kbastani
 */
public enum TodoStatus {
    CREATED,
    ACTIVE,
    COMPLETED
}
