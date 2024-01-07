package Models;
import TaskManager.TaskStatus;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(Integer id, String name, String description, TaskStatus status) {

        super(id, name, description, status);

    }

    public ArrayList<Integer> getSubtasksId() {

        return subtasksId;

    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {

        this.subtasksId = subtasksId;

    }

}
