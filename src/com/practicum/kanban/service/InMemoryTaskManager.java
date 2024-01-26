package com.practicum.kanban.service;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected Integer id = 0;
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected final Map<Integer, Epic> allEpics = new HashMap<>();
    protected final Map<Integer, Subtask> allSubtasks = new HashMap<>();

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

        Epic epic = allEpics.get(subtask.getEpicId());
        List<Integer> subtasks = epic.getSubtasksId();
        subtasks.add(subtask.getId());
        calculateEpicStatus(epic);
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
        for (Epic epic : allEpics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(TaskStatus.NEW);
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

        allTasks.put(id, task);

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
        Epic epic = allEpics.get(subtask.getEpicId());
        calculateEpicStatus(epic);

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

        for (Integer subtaskId : epic.getSubtasksId()) {
            allSubtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }

        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask deletingSubtask = allSubtasks.remove(id);
        int epicId = deletingSubtask.getEpicId();
        Epic epic = allEpics.get(epicId);
        List<Integer> subtasks = epic.getSubtasksId();

        subtasks.remove((Integer) id);
        historyManager.remove(id);
        calculateEpicStatus(epic);
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

    private Integer assignId() {
        return ++this.id;
    }

    private void calculateEpicStatus(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        int quantitySubtasks = epic.getSubtasksId().size();

        for (Integer subtaskId : epic.getSubtasksId()) {
            final TaskStatus status = allSubtasks.get(subtaskId).getStatus();
            if (status == TaskStatus.NEW) {
                countNew++;
            } else if (status == TaskStatus.DONE) {
                countDone++;
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        if (countNew == quantitySubtasks) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDone == quantitySubtasks) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}