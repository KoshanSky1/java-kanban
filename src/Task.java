import java.util.Objects;

public class Task {
    public String name;
    public String description;
    protected int id;
    protected String status;

    public Task(String name, String description, Integer id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "{name= " + name + ", description= " + description + ", id= " + id + ", status= " + status + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
        && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}