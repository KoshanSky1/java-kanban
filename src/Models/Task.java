package Models;
import TaskManager.TaskStatus;
import java.util.Objects;

public class Task {

    private Integer id;

    private String name;

    private String description;

    private TaskStatus status;



    public Task(Integer id, String name, String description, TaskStatus status) {

        this.id = id;

        this.name = name;

        this.description = description;

        this.status = status;

    }



    public int getId() {

        return id;

    }



    public void setId(int id) {

        this.id = id;

    }



    public String getName() {

        return name;

    }



    public void setName(String name) {

        this.name = name;

    }



    public String getDescription() {

        return description;

    }



    public void setDescription(String description) {

        this.description = description;

    }



    public TaskStatus getStatus() {

        return status;

    }



    public void setStatus(TaskStatus status) {

        this.status = status;

    }



    @Override

    public String toString() {

        String result = "{name= " + name; //+ ", description= " + description

        if(description != null) {

            result = result + ", description.lenght= " + description.length();

        }

        else {

            result = result + ", description.lenght= null";

        }

        result = result + ", id= " + id + ", status= " + status + "}";

        return result;

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
