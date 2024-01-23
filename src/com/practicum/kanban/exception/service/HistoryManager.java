package com.practicum.kanban.exception.service;
import com.practicum.kanban.exception.model.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}