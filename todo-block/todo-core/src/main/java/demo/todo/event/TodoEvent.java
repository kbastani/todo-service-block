package demo.todo.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demo.todo.Todo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Map;

/**
 * The domain event {@link TodoEvent} tracks the type and state of events as applied to the {@link Todo} domain
 * object. This event resource can be used to event source the aggregate state of {@link Todo}.
 * <p>
 * This event resource also provides a transaction log that can be used to append actions to the event.
 *
 * @author Kenny Bastani
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TodoEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    private TodoEventType type;

    @Transient
    @JsonIgnore
    private Todo entity;

    @Transient
    private Map<String, Object> payload;

    private Long todoId;

    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long lastModified;

    public TodoEvent() {
    }

    public TodoEvent(Long eventId) {
        this.eventId = eventId;
    }

    public TodoEvent(TodoEventType type) {
        this.type = type;
    }

    public TodoEvent(TodoEventType type, Long todoId) {
        this.type = type;
        this.todoId = todoId;
    }

    public TodoEvent(TodoEventType type, Todo entity) {
        this.type = type;
        this.entity = entity;
        this.todoId = entity.getIdentity();
    }

    public TodoEvent(TodoEventType type, Todo entity, Map<String, Object> payload) {
        this.type = type;
        this.entity = entity;
        this.payload = payload;
        this.todoId = entity.getIdentity();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long id) {
        eventId = id;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public TodoEventType getType() {
        return type;
    }

    public void setType(TodoEventType type) {
        this.type = type;
    }

    public Todo getEntity() {
        return entity;
    }

    public void setEntity(Todo entity) {
        this.entity = entity;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "TodoEvent{" +
                "eventId=" + eventId +
                ", type=" + type +
                ", entity=" + entity +
                ", payload=" + (payload != null ? String.valueOf(payload.hashCode()) : null) +
                ", todoId=" + todoId +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }
}
