package TaskManager;
import Models.Epic;
import Models.Subtask;
import Models.Task;
import java.util.ArrayList;

public interface TaskManager {

    public Integer assignID();

    public void createNewTask(Task task);

    public void createNewEpic(Epic epic);

    public void createNewSubtask(Subtask subtask);

    public ArrayList<Task> getAllTasks();

    public ArrayList<Epic> getAllEpics();

    public ArrayList<Subtask> getAllSubtasks();

    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    public Task getTaskByID(int id);

    public Epic getEpicByID(int id);

    public Epic getEpicByIdService(int id);

    public Subtask getSubtaskByID(int id);

    public Task updateTask(int id, Task task);

    public Epic updateEpic(int id, Epic epic);

    public Subtask updateSubtask(int id, Subtask subtask);

    public void deleteTaskByID(int id);

    public void deleteEpicByID(int id);

    public void deleteSubtaskByID(int id);

    public ArrayList<Subtask> getSubtasksByEpic(int id);

    public Integer getId();

    public void setId(Integer id);

    ArrayList<Task> getHistory();

}