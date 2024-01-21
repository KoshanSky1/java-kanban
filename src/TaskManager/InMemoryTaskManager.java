package TaskManager;
import Models.Epic;
import Models.Subtask;
import Models.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private Integer id = 0;
    protected final HashMap<Integer, Task> allTasks = new HashMap<>(); // хранение данных
    protected final HashMap<Integer, Epic> allEpics = new HashMap<>();
    protected final HashMap<Integer, Subtask> allSubtasks = new HashMap<>();

    @Override
    public Integer assignID() { // метод присваивает id задачам любого из типов

        this.id++;

        return this.id;

    }

    @Override

    public void createNewTask(Task task) { // создание задач по типам

        allTasks.put(task.getId(), task);

    }

    @Override
    public void createNewEpic(Epic epic) {

        allEpics.put(epic.getId(), epic);

    }

    @Override
    public void createNewSubtask(Subtask subtask) {

        allSubtasks.put(subtask.getId(), subtask);

        Epic epic = getEpicByIdService(subtask.getEpicID());

        epic.getSubtasksId().add(subtask.getId());

        int countNew = 0;

        int countDone = 0;

        for (Integer ID : epic.getSubtasksId()) {

            subtask = allSubtasks.get(ID);

            if (subtask.getStatus() == TaskStatus.NEW) {

                countNew = countNew + 1;

            } else if (subtask.getStatus() == TaskStatus.DONE) {

                countDone = countDone + 1;

            }

        }

        if (countNew == epic.getSubtasksId().size() || epic.getSubtasksId().size() == 0.0) {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.NEW);

            allEpics.put(epic.getId(), epic);


        } else if (countDone == epic.getSubtasksId().size()) {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.DONE);

            allEpics.put(epic.getId(), epic);

        } else {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.IN_PROGRESS);

            allEpics.put(epic.getId(), epic);

        }

    }

    @Override
    public ArrayList<Task> getAllTasks() { // получение всех задач (по типам)

        ArrayList<Task> tasksRegister = new ArrayList<>();

        for (Integer id : allTasks.keySet()) {

            tasksRegister.add(allTasks.get(id));

        }

        return tasksRegister;

    }

    @Override
    public ArrayList<Epic> getAllEpics() {

        ArrayList<Epic> epicRegister = new ArrayList<>();

        for (Integer id : allEpics.keySet()) {

            epicRegister.add(allEpics.get(id));

        }

        return epicRegister;

    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {

        ArrayList<Subtask> subtasksRegister = new ArrayList<>();

        for (Integer id : allSubtasks.keySet()) {

            subtasksRegister.add(allSubtasks.get(id));

        }

        return subtasksRegister;

    }

    @Override
    public void deleteAllTasks() { // удаление всех задач (по типам)

        allTasks.clear();

    }

    @Override
    public void deleteAllEpics() {

        allEpics.clear();

        allSubtasks.clear();

    }

    @Override
    public void deleteAllSubtasks() {

        allSubtasks.clear();

    }

    @Override
    public Task getTaskByID(int id) { // получение задач по идентификатору (по типам)

        Task taskByID = allTasks.get(id);

        if (taskByID != null) {

            historyManager.add(taskByID);

        }

        return taskByID;

    }

    @Override

    public Epic getEpicByID(int id) {

        Epic epicByID = allEpics.get(id);

        historyManager.add(epicByID);

        return epicByID;

    }

    @Override
    public Epic getEpicByIdService(int id) {

        Epic epicById = allEpics.get(id);

        return epicById;

    }

    @Override
    public Subtask getSubtaskByID(int id) {

        Subtask subtaskByID = allSubtasks.get(id);

        historyManager.add(subtaskByID);

        return subtaskByID;

    }

    @Override

    public Task updateTask(int id, Task task) {

        allTasks.remove(id);

        allTasks.put(id, task);

        return task;

    }

    @Override
    public Epic updateEpic(int id, Epic epic) {

        allEpics.remove(id);

        allEpics.put(id, epic);

        return epic;

    }

    @Override
    public Subtask updateSubtask(int id, Subtask subtask) {

        allSubtasks.remove(id);

        allSubtasks.put(id, subtask);

        int countNew = 0;

        int countDone = 0;

        Epic epic = allEpics.get(subtask.getEpicID());

        for (Integer ID : epic.getSubtasksId()) {

            subtask = allSubtasks.get(ID);

            if (subtask.getStatus() == TaskStatus.NEW) {

                countNew = countNew + 1;

            } else if (subtask.getStatus() == TaskStatus.DONE) {

                countDone = countDone + 1;

            }

        }

        if (countNew == epic.getSubtasksId().size() || epic.getSubtasksId().size() == 0.0) {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.NEW);

            allEpics.put(epic.getId(), epic);

        } else if (countDone == epic.getSubtasksId().size() && countDone != 0) {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.DONE);

            allEpics.put(epic.getId(), epic);

        } else {

            allEpics.remove(epic.getId());

            epic.setStatus(TaskStatus.IN_PROGRESS);

            allEpics.put(epic.getId(), epic);

        }

        return subtask;

    }

    @Override
    public void deleteTaskByID(int id) { //удаление задач по идентификатору (по типам)

        allTasks.remove(id);

        historyManager.remove(id);

    }

    @Override
    public void deleteEpicByID(int id) {

        Epic epic = allEpics.get(id);

        for (Integer ID : epic.getSubtasksId()) {

            allSubtasks.remove(ID);

            historyManager.remove(ID);

        }

        allEpics.remove(id);

        historyManager.remove(id);

    }

    @Override
    public void deleteSubtaskByID(int id) {

        allSubtasks.remove(id);

        historyManager.remove(id);

    }

    @Override

    public ArrayList<Subtask> getSubtasksByEpic(int id) { //Получение списка всех подзадач определённого эпика

        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();

        Epic epic = allEpics.get(id);

        for (Integer ID : epic.getSubtasksId()) {

            subtasksByEpic.add(allSubtasks.get(ID));

        }

        return subtasksByEpic;

    }

    @Override

    public Integer getId() {

        return id;

    }

    @Override
    public void setId(Integer id) {

        this.id = id;

    }

    @Override
    public ArrayList<Task> getHistory() {

        return historyManager.getHistory();

    }

}