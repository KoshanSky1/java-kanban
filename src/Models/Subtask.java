package Models;

public class Subtask extends Task {

    private Integer epicID;

    public Subtask(Integer id, String name, String description, String status, Integer epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }
}
