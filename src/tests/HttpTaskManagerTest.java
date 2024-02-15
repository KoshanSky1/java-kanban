package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.server.HttpTaskManager;
import com.practicum.kanban.server.KVServer;
import com.practicum.kanban.service.InMemoryTaskManager;
import com.practicum.kanban.service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {

    private KVServer server;
    private TaskManager taskManager;
    private Task taskNumberOne;
    private Epic epicNumberOne;
    private Subtask subtaskNumberOne;
    private Subtask subtaskNumberTwo;

    @BeforeEach
    void addManager() {
        try {
            server = new KVServer();
            server.start();
            taskManager = new InMemoryTaskManager();
            taskNumberOne = new Task(null, "Task №1",
                    "description of Task №1", TaskStatus.NEW,
                    null,
                    Duration.ofMinutes(10));
            taskManager.createNewTask(taskNumberOne);

            epicNumberOne = new Epic(null, "Epic №1",
                    "description of Epic №1", TaskStatus.NEW,
                    null, null);
            taskManager.createNewEpic(epicNumberOne);

            subtaskNumberOne = new Subtask(3, "Subtask №1",
                    "description of Subtask №1", TaskStatus.NEW,
                    LocalDateTime.of(2024, 5, 1, 10, 0),
                    Duration.ofMinutes(10), epicNumberOne.getId());
            taskManager.createNewSubtask(subtaskNumberOne);

            subtaskNumberTwo = new Subtask(4, "Subtask №3",
                    "description of Subtask №3", TaskStatus.IN_PROGRESS,
                    LocalDateTime.of(2028, 5, 1, 10, 0),
                    Duration.ofMinutes(10), epicNumberOne.getId());
            taskManager.createNewSubtask(subtaskNumberTwo);

            taskManager.getTaskById(1);
            taskManager.getEpicById(2);
            taskManager.getSubtaskById(4);
            taskManager.getSubtaskById(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveAndLoad() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(Path.of("http://localhost:8080"));

        assertEquals(taskManager.getAllTasks(), httpTaskManager.getAllTasks(),
                "Списки задач не совпадают");
        assertEquals(taskManager.getAllEpics(), httpTaskManager.getAllEpics(),
                "Списки эпиков не совпадают");
        assertEquals(taskManager.getAllSubtasks(), httpTaskManager.getAllSubtasks(),
                "Списки подзадач не совпадают");
        assertEquals(taskManager.getHistory(), httpTaskManager.getHistory(),
                "Списки истории не совпадают");
    }
}