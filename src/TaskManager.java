import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    private Integer id = 0;

    public Integer assignID() { // метод присваивает id задачам любого из типов
        this.id++;
        return this.id;
    }

    HashMap <Integer, Task> allTasks = new HashMap<>(); // создание и хранение данных

    public Task createNewTask(Task task) {
        allTasks.put(task.id, task);
        return task;
    }

    HashMap <Integer, Epic> allEpics = new HashMap<>();

    public Epic createNewEpic(Epic epic) {
        allEpics.put(epic.id, epic);
        return epic;
    }

    HashMap <Integer, Subtask> allSubtasks = new HashMap<>();

    public Subtask createNewSubtask(Subtask subtask) {
        allSubtasks.put(subtask.id, subtask);
        Epic epic = getEpicByID(subtask.epicID);
        epic.subtasks.add(subtask.id);

        int countNew = 0;
        int countDone = 0;

        for (Integer ID : epic.subtasks) {
            subtask = allSubtasks.get(ID);
            if (subtask.status.equals("NEW")) {
                countNew = countNew + 1;
            } else if (subtask.status.equals("DONE")); {
                countDone = countDone + 1;
            }
        }

        if (countNew == epic.subtasks.size() || epic.subtasks.size() == 0) {
            allEpics.remove(epic.id);
            epic.status = "NEW";
            allEpics.put(epic.id, epic);
        } else if (countDone == epic.subtasks.size()) {
            allEpics.remove(epic.id);
            epic.status = "DONE";
            allEpics.put(epic.id, epic);
        } else {
            allEpics.remove(epic.id);
            epic.status = "IN_PROGRESS";
            allEpics.put(epic.id, epic);
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

        Epic epic = allEpics.get(subtask.epicID);

        for (Integer ID : epic.subtasks) {
            subtask = allSubtasks.get(ID);
            if (subtask.status.equals("NEW")) {
                countNew = countNew + 1;
            } else if (subtask.status.equals("DONE")); {
                countDone = countDone + 1;
            }
       }
       if (countNew == epic.subtasks.size() || epic.subtasks.size() == 0) {
           allEpics.remove(epic.id);
           epic.status = "NEW";
           allEpics.put(epic.id, epic);
       } else if (countDone == epic.subtasks.size()) {
           allEpics.remove(epic.id);
           epic.status = "DONE";
           allEpics.put(epic.id, epic);
       } else {
           allEpics.remove(epic.id);
           epic.status = "IN_PROGRESS";
           allEpics.put(epic.id, epic);
       }
    return subtask;
    }


    public void deleteTaskByID(int id) { //удаление задач по идентификатору (по типам)
        allTasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        allEpics.remove(id);
    }

    public void deleteSubtaskByID(int id) {
        allSubtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic(int id) { //Получение списка всех подзадач определённого эпика
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        Epic epic = allEpics.get(id);
        for (Integer ID : epic.subtasks) {
        subtasksByEpic.add(allSubtasks.get(ID));
        }
        return subtasksByEpic;
    }
}





