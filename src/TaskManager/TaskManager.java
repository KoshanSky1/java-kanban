package TaskManager;

import Models.Epic;
import Models.Subtask;
import Models.Task;

import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    private Integer id = 0;

    private HashMap <Integer, Task> allTasks = new HashMap<>(); // хранение данных
    private HashMap <Integer, Epic> allEpics = new HashMap<>();
    private HashMap <Integer, Subtask> allSubtasks = new HashMap<>();

    public Integer assignID() { // метод присваивает id задачам любого из типов
        this.id++;
        return this.id;
    }

    public Task createNewTask(Task task) { // создание задач по типам
        allTasks.put(task.getId(), task);
        return task;
    }

    public Epic createNewEpic(Epic epic) {
        allEpics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createNewSubtask(Subtask subtask) {
        allSubtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicByID(subtask.getEpicID());
        epic.getSubtasksIDs().add(subtask.getId());

        int countNew = 0;
        int countDone = 0;

        for (Integer ID : epic.getSubtasksIDs()) {
            subtask = allSubtasks.get(ID);
            if (subtask.getStatus().equals("NEW")) {
                countNew = countNew + 1;
            } else if (subtask.getStatus().equals("DONE")) {
                countDone = countDone + 1;
            }
        }

        if (countNew == epic.getSubtasksIDs().size() || epic.getSubtasksIDs().size() == 0.0) {
            allEpics.remove(epic.getId());
            epic.setStatus("NEW");
            allEpics.put(epic.getId(), epic);
        } else if (countDone == epic.getSubtasksIDs().size()) {
            allEpics.remove(epic.getId());
            epic.setStatus("DONE");
            allEpics.put(epic.getId(), epic);
        } else {
            allEpics.remove(epic.getId());
            epic.setStatus("IN_PROGRESS");
            allEpics.put(epic.getId(), epic);
        }
        return subtask;
    }

     public ArrayList<Task> getAllTasks() { // получение всех задач (по типам)
        ArrayList<Task> tasksRegister = new ArrayList<>();
        for (Integer id : allTasks.keySet()) {
             tasksRegister.add(allTasks.get(id));
         }
         return tasksRegister;
     }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicRegister = new ArrayList<>();
        for (Integer id : allEpics.keySet()) {
            epicRegister.add(allEpics.get(id));
        }
        return epicRegister;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasksRegister = new ArrayList<>();
        for (Integer id : allSubtasks.keySet()) {
            subtasksRegister.add(allSubtasks.get(id));
        }
        return subtasksRegister;
    }

    public void deleteAllTasks() { // удаление всех задач (по типам)
        allTasks.clear();
    }

    public void deleteAllEpics() {
        allEpics.clear();
        allSubtasks.clear();
    }

    public void deleteAllSubtasks() {
        allSubtasks.clear();
    }

    public Task getTaskByID(int id) { // получение задач по идентификатору (по типам)
        Task taskByID = allTasks.get(id);
        return taskByID;
    }

    public Epic getEpicByID(int id) {
        Epic epicByID = allEpics.get(id);
        return epicByID;
    }

    public Subtask getSubtaskByID(int id) {
        Subtask subtaskByID = allSubtasks.get(id);
        return subtaskByID;
    }

    public Task updateTask(int id, Task task) { // обновление задач (по типам)
        allTasks.remove(id);
        allTasks.put(id, task);
        return task;
    }

    public Epic updateEpic(int id, Epic epic) {
        allEpics.remove(id);
        allEpics.put(id, epic);
        return epic;
    }

    public Subtask updateSubtask(int id, Subtask subtask) {
        allSubtasks.remove(id);
        allSubtasks.put(id, subtask);

        int countNew = 0;
        int countDone = 0;

        Epic epic = allEpics.get(subtask.getEpicID());

        for (Integer ID : epic.getSubtasksIDs()) {
            subtask = allSubtasks.get(ID);
            if (subtask.getStatus().equals("NEW")) {
                countNew = countNew + 1;
            } else if (subtask.getStatus().equals("DONE")) {
                countDone = countDone + 1;
            }
       }
       if (countNew == epic.getSubtasksIDs().size() || epic.getSubtasksIDs().size() == 0.0) {
           allEpics.remove(epic.getId());
           epic.setStatus("NEW");
           allEpics.put(epic.getId(), epic);
       } else if (countDone == epic.getSubtasksIDs().size() && countDone != 0 ) {
           allEpics.remove(epic.getId());
           epic.setStatus("DONE");
           allEpics.put(epic.getId(), epic);
       } else {
           allEpics.remove(epic.getId());
           epic.setStatus("IN_PROGRESS");
           allEpics.put(epic.getId(), epic);
       }
    return subtask;
    }

    public void deleteTaskByID(int id) { //удаление задач по идентификатору (по типам)
        allTasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        Epic epic = allEpics.get(id);
        allEpics.remove(id);
        epic.getSubtasksIDs().clear();
    }

    public void deleteSubtaskByID(int id) {
        allSubtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic(int id) { //Получение списка всех подзадач определённого эпика
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        Epic epic = allEpics.get(id);
        for (Integer ID : epic.getSubtasksIDs()) {
            subtasksByEpic.add(allSubtasks.get(ID));
        }
        return subtasksByEpic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAllTasks(HashMap<Integer, Task> allTasks) {
        this.allTasks = allTasks;
    }

    public void setAllEpics(HashMap<Integer, Epic> allEpics) {
        this.allEpics = allEpics;
    }

    public void setAllSubtasks(HashMap<Integer, Subtask> allSubtasks) {
        this.allSubtasks = allSubtasks;
    }
}





