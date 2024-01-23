package com.practicum.kanban.exception.service;
import com.practicum.kanban.exception.model.Epic;
import com.practicum.kanban.exception.model.Subtask;
import com.practicum.kanban.exception.model.Task;
import com.practicum.kanban.exception.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private Integer id = 0;
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected final Map<Integer, Epic> allEpics = new HashMap<>();
    protected final Map<Integer, Subtask> allSubtasks = new HashMap<>();

    private Integer assignId() {

        return ++this.id;

    }

    @Override
    public void createNewTask(Task task) { // создание задач по типам

        task.setId(assignId());
        allTasks.put(task.getId(), task);

    }

    @Override
    public void createNewEpic(Epic epic) {

        epic.setId(assignId());
        allEpics.put(epic.getId(), epic);

    }

    @Override
    public void createNewSubtask(Subtask subtask) {

        subtask.setId(assignId());

        allSubtasks.put(subtask.getId(), subtask);

        calculateEpicStatus(subtask);

    }

    private void calculateEpicStatus (Subtask subtask) {

    Epic epic = allEpics.get(subtask.getEpicId());

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
    public List<Task> getAllTasks() {

        return new ArrayList<>(allTasks.values());

    }

    @Override
    public List<Epic> getAllEpics() {

        return new ArrayList<>(allEpics.values());

    }

    @Override
    public List<Subtask> getAllSubtasks() {

        return new ArrayList<>(allSubtasks.values());

    }

    @Override
    public void deleteAllTasks() {

        for (Integer id : allTasks.keySet()) {

            historyManager.remove(id);

        }

        allTasks.clear();

    }

    @Override
    public void deleteAllEpics() {

        for (Integer id : allEpics.keySet()) {

            historyManager.remove(id);

        }

        allEpics.clear();

        for (Integer id : allSubtasks.keySet()) {

            historyManager.remove(id);

        }

        allSubtasks.clear();

    }

    @Override
    public void deleteAllSubtasks() {

        for (Subtask subtask : allSubtasks.values()) {

            int id = subtask.getEpicId();
            Epic epic = allEpics.get(id);
            epic.setStatus(TaskStatus.NEW);
            List<Integer> subtasks = epic.getSubtasksId();
            subtasks.clear();

        }

        for (Integer id : allSubtasks.keySet()) {

            historyManager.remove(id);

        }

        allSubtasks.clear();

    }

    @Override
    public Task getTaskById(int id) { // получение задач по идентификатору (по типам)

        Task taskByID = allTasks.get(id);

        if (taskByID != null) {

            historyManager.add(taskByID);

        }

        return taskByID;

    }

    @Override
    public Epic getEpicById(int id) {

        Epic epicByID = allEpics.get(id);

        if (epicByID != null) {

            historyManager.add(epicByID);
        }

        return epicByID;

    }

    @Override
    public Subtask getSubtaskById(int id) {

        Subtask subtaskByID = allSubtasks.get(id);

        if (subtaskByID != null) {

            historyManager.add(subtaskByID);
        }

            return subtaskByID;

    }

    @Override
    public Task updateTask(Task task) {

        int id = task.getId();

        allTasks.put(id,task);

        return task;

    }

    @Override
    public Epic updateEpic(Epic epic) {

        Epic oldEpic = allEpics.get(epic.getId());

        oldEpic.setDescription(epic.getDescription());

        oldEpic.setName(epic.getName());

        return oldEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {

        int id = subtask.getId();

        allSubtasks.put(id, subtask);

        calculateEpicStatus(subtask);

        return subtask;

    }

    @Override
    public void deleteTaskById(int id) { //удаление задач по идентификатору (по типам)

        allTasks.remove(id);

        historyManager.remove(id);

    }

    @Override
    public void deleteEpicById(int id) {

        Epic epic = allEpics.remove(id);

        for (Integer identifier : epic.getSubtasksId()) {

            allSubtasks.remove(identifier);

            historyManager.remove(identifier);

        }

        historyManager.remove(id);

    }

    @Override
    public void deleteSubtaskById(int id) {

        Subtask deletingSubtask = allSubtasks.get(id);
        int epicId = deletingSubtask.getEpicId();
        Epic epic = allEpics.get(epicId);
        List<Integer> subtasks = epic.getSubtasksId();

        subtasks.remove((Integer) id);

        allSubtasks.remove(id);
        historyManager.remove(id);

        Integer index = subtasks.get(0);
        Subtask subtask = allSubtasks.get(index);
        System.out.println(subtask);

        if (subtask != null) {

            calculateEpicStatus(subtask);

        } else {

            epic.setStatus(TaskStatus.NEW);

       }

    }

    @Override
    public List<Subtask> getSubtasksByEpic(int id) {

        List<Subtask> subtasksByEpic = new ArrayList<>();

        Epic epic = allEpics.get(id);

        for (Integer identifier : epic.getSubtasksId()) {

            subtasksByEpic.add(allSubtasks.get(identifier));

        }

        return subtasksByEpic;

    }


    @Override
    public Integer getId() {

        return id;

    }


    @Override
    public List<Task> getHistory() {

        return historyManager.getHistory();

    }

}