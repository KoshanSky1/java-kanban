public class Subtask extends Task {
    Integer epicID;

    public Subtask(String name, String description, Integer id, String status, Integer epicID) {
        super(name, description, id, status);
        this.epicID = epicID;
    }
}
