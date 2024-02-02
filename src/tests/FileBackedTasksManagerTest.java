package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.service.FileBackedTasksManager;
import com.practicum.kanban.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.practicum.kanban.service.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void addManager() {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        super.manager = new FileBackedTasksManager(fileKanban.toPath());
        addTestTasks();
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        manager.getSubtaskById(4);
    }

    @Test
    void saveAndLoad() {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        InMemoryTaskManager fileManager = new FileBackedTasksManager(fileKanban.toPath());
        List<Task> taskListManager = manager.getAllTasks();
        List<Epic> epicListManager = manager.getAllEpics();
        List<Subtask> subtasksListManager = manager.getAllSubtasks();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        List<Task> historyListManager = manager.getHistory();

        loadFromFile(new File("C:/Users/StrateX/dev/java-kanban/kanban.csv"));
        List<Task> taskListFileManager = fileManager.getAllTasks();
        List<Epic> epicListFileManager = fileManager.getAllEpics();
        List<Subtask> subtasksListFileManager = fileManager.getAllSubtasks();
        List<Task> historyListFileManager = fileManager.getHistory();
        assertEquals(taskListManager, taskListFileManager, "Списки задач не совпадают");
        assertEquals(epicListManager, epicListFileManager, "Списки эпиков не совпадают");
        assertEquals(subtasksListManager, subtasksListFileManager, "Списки подзадач не совпадают");
        assertEquals(historyListManager, historyListFileManager, "Списки истории не совпадают");
    }


    @Test
    void saveAndLoadWithEmptyListOfTaskAndWithEmptyListOfHistory() {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        InMemoryTaskManager fileManager = new FileBackedTasksManager(fileKanban.toPath());
        loadFromFile(new File("C:/Users/StrateX/dev/java-kanban/kanban.csv"));
        List<Task> taskListFileManager = fileManager.getAllTasks();
        List<Epic> epicListFileManager = fileManager.getAllEpics();
        List<Subtask> subtasksListFileManager = fileManager.getAllSubtasks();
        List<Task> historyListFileManager = fileManager.getHistory();
        assertNull(taskListFileManager, "Список задач пустой");
        assertNull(epicListFileManager, "Список эпиков пустой");
        assertNull(subtasksListFileManager, "Список подзадач пустой");
        assertNull(historyListFileManager, "Список истории не пустой");
    }

}