package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.service.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void addManager() {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        super.manager = new FileBackedTasksManager(fileKanban.toPath());
        addTestTasks();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
    }

    @Test
    void saveAndLoad() {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(fileKanban);
        List<Task> taskListManager = manager.getAllTasks();
        List<Epic> epicListManager = manager.getAllEpics();
        List<Subtask> subtasksListManager = manager.getAllSubtasks();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        List<Task> historyListManager = manager.getHistory();
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
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(fileKanban);
        List<Task> taskListFileManager = fileManager.getAllTasks();
        List<Epic> epicListFileManager = fileManager.getAllEpics();
        List<Subtask> subtasksListFileManager = fileManager.getAllSubtasks();
        List<Task> historyListFileManager = fileManager.getHistory();
        assertEquals(0, taskListFileManager.size(), "Список задач пустой");
        assertEquals(0, epicListFileManager.size(), "Список эпиков пустой");
        assertEquals(0, subtasksListFileManager.size(), "Список подзадач пустой");
        assertEquals(0, historyListFileManager.size(), "Список истории не пустой");
    }

}