package demo.todo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import demo.todo.event.TodoEvent;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Todo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TodoStatus status;

    private String title;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<TodoEvent> events = new ArrayList<>();

    public Todo() {
        status = TodoStatus.ACTIVE;
    }

    public Todo(String title) {
        this();
        this.title = title;
    }

    public Todo(Long id) {
        this.id = id;
    }

    @JsonProperty("todoId")
    public Long getIdentity() {
        return this.id;
    }

    public void setIdentity(Long id) {
        this.id = id;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TodoEvent> getEvents() {
        return events;
    }

    public void setEvents(List<TodoEvent> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", events=" + events +
                '}';
    }
}
