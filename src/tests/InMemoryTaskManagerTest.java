package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.practicum.kanban.model.TaskStatus.*;
import static org.testng.Assert.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void addManager() {
        super.manager = new InMemoryTaskManager();
        addTestTasks();
    }


    @Test
    void calculateEpicStatus() {
        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", null,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(null, "Subtask №1",
                "description of Subtask №1", IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);


        Subtask subtaskNumberTwo = new Subtask(null, "Subtask №2",
                "description of Subtask №2", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(180), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);


        manager.calculateEpicStatus(epicNumberOne);
        assertEquals(TaskStatus.IN_PROGRESS, epicNumberOne.getStatus());
    }


    @Test
    void calculateEpicStatusWithoutSubtasks() {
        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", null,
                null, null);
        manager.calculateEpicStatus(epicNumberOne);
        assertEquals(NEW, epicNumberOne.getStatus());
    }

    @Test
    void calculateEpicStatusWithSllSubtasksHavingTheStatusNew() {
        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", null,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(null, "Subtask №1",
                "description of Subtask №1", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);


        Subtask subtaskNumberTwo = new Subtask(null, "Subtask №2",
                "description of Subtask №2", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(180), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        Subtask subtaskNumberThree = new Subtask(null, "Subtask №3",
                "description of Subtask №3", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 9, 15, 0),
                Duration.ofMinutes(25), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberThree);

        manager.calculateEpicStatus(epicNumberOne);
        assertEquals(NEW, epicNumberOne.getStatus());
    }


    @Test
    void calculateEpicStatusWithSllSubtasksHavingTheStatusDone() {
        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", null,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(null, "Subtask №1",
                "description of Subtask №1", TaskStatus.DONE,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);


        Subtask subtaskNumberTwo = new Subtask(null, "Subtask №2",
                "description of Subtask №2", TaskStatus.DONE,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(180), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        Subtask subtaskNumberThree = new Subtask(null, "Subtask №3",
                "description of Subtask №3", TaskStatus.DONE,
                LocalDateTime.of(2023, 3, 9, 15, 0),
                Duration.ofMinutes(25), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberThree);


        manager.calculateEpicStatus(epicNumberOne);
        assertEquals(TaskStatus.DONE, epicNumberOne.getStatus());
    }


    @Test
    void calculateEpicStatusWithSllSubtasksHavingTheInProgress() {
        Epic epicNumberOne = new Epic(null, "Epic №1",
                "description of Epic №1", null,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(null, "Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);


        Subtask subtaskNumberTwo = new Subtask(null, "Subtask №2",
                "description of Subtask №2", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(180), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        Subtask subtaskNumberThree = new Subtask(null, "Subtask №3",
                "description of Subtask №3", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 9, 15, 0),
                Duration.ofMinutes(25), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberThree);


        manager.calculateEpicStatus(epicNumberOne);
        assertEquals(TaskStatus.IN_PROGRESS, epicNumberOne.getStatus());
    }

}