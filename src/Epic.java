import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasks = new ArrayList<>();


    public Epic(String name, String description, Integer id, String status) {
        super(name, description, id, status);
   }
}
