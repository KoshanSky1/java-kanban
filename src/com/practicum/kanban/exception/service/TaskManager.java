package com.practicum.kanban.exception.service;
import com.practicum.kanban.exception.model.Epic;
import com.practicum.kanban.exception.model.Subtask;
import com.practicum.kanban.exception.model.Task;
import java.util.List;

public interface TaskManager {

    void createNewTask(Task task);

    void createNewEpic(Epic epic);

    void createNewSubtask(Subtask subtask);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksByEpic(int id);

    Integer getId();

    List<Task> getHistory();

}