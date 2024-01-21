package Models;
import TaskManager.TaskStatus;
import TaskManager.TaskTypes;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId = new ArrayList<>();
    public Epic(Integer id, String name, String description, TaskStatus status, TaskTypes type) {

        super(id, name, description, status, type);

    }

    public ArrayList<Integer> getSubtasksId() {

        return subtasksId;

    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {

        this.subtasksId = subtasksId;

    }
}