package Models;
import TaskManager.TaskStatus;
import TaskManager.TaskTypes;

public class Subtask extends Task {

    private Integer epicID;

    public Subtask(Integer id, String name, String description, TaskStatus status, TaskTypes type, Integer epicID) {

        super(id, name, description, status, type);
        this.epicID = epicID;

    }

    public Integer getEpicID() {

        return epicID;

    }

    public void setEpicID(Integer epicID) {

        this.epicID = epicID;

    }

    @Override
    public String toString() {

        String result = super.toString();
        result = result + "," + epicID;
        return result;
    }

}