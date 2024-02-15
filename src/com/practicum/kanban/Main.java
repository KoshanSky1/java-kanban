package com.practicum.kanban;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.server.KVServer;
import com.practicum.kanban.server.KVTaskClient;
import com.practicum.kanban.service.InMemoryTaskManager;
import com.practicum.kanban.service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

        new KVServer().start();
        new KVTaskClient();

        TaskManager manager = new InMemoryTaskManager();

        Task taskNumberOne = new Task(null, "Task №1",
                "description of Task №1", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));
        manager.createNewTask(taskNumberOne);

        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", TaskStatus.NEW,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(3, "Subtask №1",
                "description of Subtask №1", TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 1, 10, 0),
                Duration.ofMinutes(10), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);

        Subtask subtaskNumberTwo = new Subtask(4, "Subtask №3",
                "description of Subtask №3", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2028, 5, 1, 10, 0),
                Duration.ofMinutes(10), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

    }
}