package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.service.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task taskNumberOne;
    protected Epic epicNumberOne;
    protected Subtask subtaskNumberOne;
    protected Subtask subtaskNumberTwo;
    protected Epic epicNumberTwo;

    protected void addTestTasks() {
        taskNumberOne = new Task(1, "Task №1", "description of Task №1", TaskStatus.NEW,
                null, null);
        manager.createNewTask(taskNumberOne);

        epicNumberOne = new Epic(2, "Epic №1",
                "description of Epic №1", TaskStatus.NEW,
                null, null);
        manager.createNewEpic(epicNumberOne);

        subtaskNumberOne = new Subtask(3, "Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);

        subtaskNumberTwo = new Subtask(4, "Subtask №2",
                "description of Subtask №2", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(60), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        epicNumberTwo = new Epic(null, "Epic №2",
                "description of Epic №2", TaskStatus.DONE,
                null, null);
        manager.createNewEpic(epicNumberTwo);
    }

    @Test
    public void createNewTask() {
        final Task savedTask = manager.getTaskById(1);
        assertNotNull(taskNumberOne, "Задача не найдена.");
        assertEquals(taskNumberOne, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(taskNumberOne, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createNewEpic() {
        final Epic savedEpic = manager.getEpicById(epicNumberOne.getId());
        assertNotNull(epicNumberOne, "Эпик не найден.");
        assertEquals(epicNumberOne, savedEpic, "Эпики не совпадают.");
        assertEquals(savedEpic.getStartTime(), of(2023, 3, 8, 10, 0),
                "Время начала не совпадает");
        assertEquals(savedEpic.getEndTime(), of(2023, 3, 8, 20, 0),
                "Время окончания не совпадает");
        assertEquals(savedEpic.getStatus(), TaskStatus.IN_PROGRESS, "Статус не совпадает");
        final List<Epic> epiсs = manager.getAllEpics();
        assertNotNull(epiсs, "Эпики не возвращаются.");
        assertEquals(2, epiсs.size(), "Неверное количество эпиков.");
        assertEquals(epicNumberOne, epiсs.get(0), "Эпики не совпадают.");
    }

    @Test
    void createNewSubtask() {
        final Subtask savedSubtask = manager.getSubtaskById(subtaskNumberOne.getId());
        assertNotNull(savedSubtask, "Позадача не найдена.");
        assertEquals(subtaskNumberOne, savedSubtask, "Подзадачи не совпадают.");
        final List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtaskNumberOne, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void getAllTasks() {
        List<Task> actual = new ArrayList<>();
        actual.add(taskNumberOne);
        List<Task> expected = manager.getAllTasks();
        assertNotNull(expected, "Задачи не возвращаются.");
        assertEquals(1, expected.size(), "Неверное количество задач.");
        assertEquals(expected, actual, "Списки не совпадают.");
    }

    @Test
    void getAllTasksWithAnEmptyTaskList() {
        manager.deleteAllTasks();
        List<Task> expected = manager.getAllTasks();
        assertTrue(expected.isEmpty(), "Список задач пуст.");
    }

    @Test
    void getAllEpics() {
        List<Epic> actual = new ArrayList<>();
        actual.add(epicNumberOne);
        actual.add(epicNumberTwo);
        List<Epic> expected = manager.getAllEpics();
        assertNotNull(expected, "Эпики не возвращаются.");
        assertEquals(2, expected.size(), "Неверное количество эпиков.");
        assertEquals(expected, actual, "Списки не совпадают.");
    }

    @Test
    void getAllEpicsWithAnEmptyEpicList() {
        manager.deleteAllEpics();
        List<Epic> expected = manager.getAllEpics();
        assertTrue(expected.isEmpty(), "Список эпиков пуст.");
    }

    @Test
    void getAllSubtasks() {
        List<Subtask> actual = new ArrayList<>();
        actual.add(subtaskNumberOne);
        actual.add(subtaskNumberTwo);
        List<Subtask> expected = manager.getAllSubtasks();
        assertNotNull(expected, "Подзадачи не возвращаются.");
        assertEquals(2, expected.size(), "Неверное количество подзадач.");
        assertEquals(expected, actual, "Списки не совпадают.");
    }

    @Test
    void getAllSubtasksWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> expected = manager.getAllSubtasks();
        assertTrue(expected.isEmpty(), "Список подзадач пуст.");
    }

    @Test
    void deleteAllTasks() {
        manager.deleteAllTasks();
        List<Task> expected = manager.getAllTasks();
        assertTrue(expected.isEmpty(), "Cписок задач пуст");
    }

    @Test
    void deleteAllTasksWithAnEmptyTaskList() {
        manager.deleteAllTasks();
        List<Task> expected = manager.getAllTasks();
        assertTrue(expected.isEmpty(), "Cписок задач пуст");
    }

    @Test
    void deleteAllEpics() {
        manager.deleteAllEpics();
        List<Epic> expectedEpic = manager.getAllEpics();
        List<Subtask> expectedSubtask = manager.getAllSubtasks();
        assertTrue(expectedEpic.isEmpty(), "Cписок эпиков пуст.");
        assertTrue(expectedSubtask.isEmpty(), "Cписок подзадач пуст.");
    }

    @Test
    void deleteAllEpicsWithAnEmptyEpicList() {
        manager.deleteAllEpics();
        manager.deleteAllEpics();
        List<Epic> expectedEpic = manager.getAllEpics();
        List<Subtask> expectedSubtask = manager.getAllSubtasks();
        assertTrue(expectedEpic.isEmpty(), "Cписок эпиков пуст.");
        assertTrue(expectedSubtask.isEmpty(), "Cписок подзадач пуст.");
    }

    @Test
    void deleteAllSubtasks() {
        manager.deleteAllSubtasks();
        List<Subtask> expectedSubtask = manager.getAllSubtasks();
        assertTrue(expectedSubtask.isEmpty(), "Cписок подзадач пуст.");
        assertEquals(epicNumberOne.getStatus(), TaskStatus.NEW, "Не изменился статус эпика.");
    }

    @Test
    void deleteAllSubtasksWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> expected = manager.getAllSubtasks();
        assertTrue(expected.isEmpty(), "Cписок подзадач пуст.");
    }

    @Test
    void getTaskById() {
        Task task = manager.getTaskById(taskNumberOne.getId());
        assertNotNull(task, "Задача не найдена.");
        assertEquals(taskNumberOne, task, "Задачи не совпадают");
    }

    @Test
    void getTaskByNonexistentId() {
        Task task77 = manager.getTaskById(77);
        assertNull(task77, "Задача найдена.");
    }

    @Test
    void getTaskByIdWithAnEmptyTaskList() {
        manager.deleteAllTasks();
        Task task = manager.getTaskById(1);
        assertNull(task, "Задача найдена.");
    }

    @Test
    void getEpicById() {
        Epic epic = manager.getEpicById(epicNumberOne.getId());
        assertNotNull(epic, "Эпик не найден.");
        assertEquals(epicNumberOne, epic, "Эпики не совпадают");
    }

    @Test
    void getEpicByNonexistentId() {
        Epic epic77 = manager.getEpicById(77);
        assertNull(epic77, "Эпик найден.");
    }

    @Test
    void getEpicByIdWithAnEmptyEpicList() {
        manager.deleteAllEpics();
        Epic epic77 = manager.getEpicById(1);
        assertNull(epic77, "Эпик найден.");
    }

    @Test
    void getSubtaskById() {
        Subtask subtask = manager.getSubtaskById(3);
        assertNotNull(subtask, "Подзадача не найдена.");
        assertEquals(subtaskNumberOne, subtask, "Подзадачи не совпадают");
    }

    @Test
    void getSubtaskByNonexistentId() {
        Subtask subtask77 = manager.getSubtaskById(77);
        assertNull(subtask77, "Подзадача найдена.");
    }

    @Test
    void getSubtasksByIdWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        Subtask subtask = manager.getSubtaskById(1);
        assertNull(subtask, "Подзадача найдена.");
    }

    @Test
    void updateTask() {
        Task updatedTask = new Task(1, "Upd Task №1",
                "Upd description of Task №1", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));

        final List<Task> tasks = manager.getAllTasks();
        manager.updateTask(updatedTask);
        final Task updTask = manager.getTaskById(1);
        assertNotNull(updTask, "Задача не найдена.");
        assertEquals(updTask, updatedTask, "Задача не обновилась");
        final List<Task> tasksUpdate = manager.getAllTasks();
        assertEquals(tasksUpdate.size(), tasks.size(), "Изменился размер списка задач.");
    }

    @Test
    void updateTaskWithAnEmptyTaskList() {
        manager.deleteAllTasks();
        Task updatedTask = new Task(1, "Upd Task №1",
                "Upd description of Task №1", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));

        manager.updateTask(updatedTask);
        assertNotNull(updatedTask, "Задача не найдена.");
        final List<Task> tasksUpdate = manager.getAllTasks();
        assertEquals(tasksUpdate.size(), 1, "Изменился размер списка задач.");
    }

    @Test
    void updateTaskWithNonexistentId() {
        Task updatedTask = new Task(77, "Upd Task №77",
                "Upd description of Task №77", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));

        manager.updateTask(updatedTask);
        final List<Task> tasksUpdate = manager.getAllTasks();
        assertEquals(tasksUpdate.size(), 2, "Изменился размер списка задач.");
        assertEquals(tasksUpdate.get(1), updatedTask, "Задача не добавилась.");
    }

    @Test
    void updateEpic() {
        Epic epicUpdate = new Epic(2, "Upd Epic №1",
                "Upd description of Epic №1", null,
                null, null);

        final List<Epic> epics = manager.getAllEpics();
        final List<Subtask> subtasks = manager.getAllSubtasks();
        manager.updateEpic(epicUpdate);
        final Epic updatedEpic = manager.getEpicById(2);
        assertNotNull(epicUpdate, "Эпик не найден.");
        assertEquals(epicUpdate.getId(), updatedEpic.getId(), "Эпик не обновился");
        final List<Epic> epicsUpdate = manager.getAllEpics();
        assertEquals(epics.size(), epicsUpdate.size(), "Изменился размер списка эпиков.");
        final List<Subtask> subtasksUpdate = manager.getAllSubtasks();
        assertEquals(subtasks, subtasksUpdate, "Изменился список подзадач.");
    }

    @Test
    void updateSubtask() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask updatedSubtask = new Subtask(3, "Upd Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        manager.updateSubtask(updatedSubtask);
        assertNotNull(updatedSubtask, "Подзадача не найдена.");
        assertEquals(manager.getSubtaskById(3), updatedSubtask, "Подзадача не обновилась");
        List<Subtask> subtasksUpd = manager.getAllSubtasks();
        Integer epicIdUpd = updatedSubtask.getEpicId();
        assertEquals(2, epicIdUpd, "Изменился идентификатор эпика.");
    }

    @Test
    void updateSubtaskWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask subtaskUpdate = new Subtask(28, "Upd Subtask №1",
                "Upd description of Subtask №1", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        manager.updateSubtask(subtaskUpdate);
        List<Subtask> subtasksUpd = manager.getAllSubtasks();
        Subtask updatedSubtask = manager.getSubtaskById(28);
        assertNotNull(updatedSubtask, "Подзадача найдена.");
        assertEquals(1, subtasksUpd.size(), "Изменился размер списка подзадач.");
    }

    @Test
    void updateSubtaskWithNonexistentId() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask subtaskUpdate = new Subtask(77, "Upd Subtask №1",
                "Upd description of Subtask №1", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        manager.updateSubtask(subtaskUpdate);
        List<Subtask> subtasksUpd = manager.getAllSubtasks();
        assertEquals(3, subtasksUpd.size(), "Не изменился размер списка подзадач.");
        Integer epicIdUpd = subtaskUpdate.getEpicId();
        assertEquals(2, epicIdUpd, "Найден идентификатор эпика.");
    }

    @Test
    void deleteTaskById() {
        manager.deleteTaskById(1);
        List<Task> tasksDel = manager.getAllTasks();
        assertEquals(0, tasksDel.size(), "Задача не удалилась");
    }

    @Test
    void deleteTaskByIdWithAnEmptyTaskList() {
        manager.deleteAllTasks();
        List<Task> tasks = manager.getAllTasks();
        manager.deleteTaskById(1);
        List<Task> tasksDel = manager.getAllTasks();
        assertEquals(tasks.size(), tasksDel.size(), "Списки задач пусты");
    }

    @Test
    void deleteTaskByIdWithNonexistentId() {
        List<Task> tasks = manager.getAllTasks();
        manager.deleteTaskById(77);
        List<Task> tasksDel = manager.getAllTasks();
        assertEquals(tasks, tasksDel, "Списки задач различаются.");
    }

    @Test
    void deleteEpicById() {
        manager.deleteEpicById(2);
        List<Epic> epicsDel = manager.getAllEpics();
        assertEquals(1, epicsDel.size(), "Эпик не удалился");
        List<Subtask> subtasksDel = manager.getAllSubtasks();
        assertEquals(0, subtasksDel.size(), "Подзадачи не удалились");
    }

    @Test
    void deleteEpicByIdWithAnEmptyEpicList() {
        manager.deleteAllEpics();
        List<Epic> epics = manager.getAllEpics();
        List<Subtask> subtasks = manager.getAllSubtasks();
        manager.deleteTaskById(2);
        List<Epic> epicsDel = manager.getAllEpics();
        List<Subtask> subtaskssDel = manager.getAllSubtasks();
        assertEquals(0, epicsDel.size(), "Список эпиков пуст.");
        assertEquals(0, subtaskssDel.size(), "Подзадачи не существовуют с пустым списком эпиков.");
    }

    @Test
    void deleteSubtaskById() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        manager.deleteSubtaskById(3);
        List<Subtask> subtasksDel = manager.getAllSubtasks();
        assertEquals(1, subtasksDel.size(), "Подзадача не удалились");
    }

    @Test
    void getSubtasksByEpic() {
        List<Subtask> actual = new ArrayList<>();
        actual.add(subtaskNumberOne);
        actual.add(subtaskNumberTwo);
        List<Subtask> expected = manager.getSubtasksByEpic(2);
        assertEquals(actual, expected, "Списки подзадач различаются.");
    }

    @Test
    void getSubtasksByEpicWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> expected = manager.getSubtasksByEpic(2);
        assertEquals(0, expected.size(), "Список подзадач пустой.");
    }

    @Test
    void getHistory() {
        manager.getTaskById(taskNumberOne.getId());
        manager.getEpicById(epicNumberOne.getId());
        manager.getSubtaskById(subtaskNumberOne.getId());
        manager.getSubtaskById(subtaskNumberTwo.getId());
        List<Task> actual = new ArrayList<>();
        actual.add(taskNumberOne);
        actual.add(epicNumberOne);
        actual.add(subtaskNumberOne);
        actual.add(subtaskNumberTwo);
        List<Task> expected = manager.getHistory();
        assertEquals(actual, expected, "История различается");
    }

    @Test
    void getHistoryWithAnEmptySubtaskList() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        List<Task> expected = manager.getHistory();
        assertEquals(0, expected.size(), "История пуста.");
    }

    @Test
    void checkTheIntersectionOfTasks() {
        Subtask subtask = new Subtask(null, "Subtask",
                "checkTheIntersectionOfTasks", TaskStatus.IN_PROGRESS,
                of(2023, 3, 8, 19, 30),
                Duration.ofMinutes(60), epicNumberOne.getId());
        manager.createNewSubtask(subtask);

        Task task = new Task(1, "Task", "checkTheIntersectionOfTasks", TaskStatus.DONE,
                of(2024, 5, 8, 19, 30),
                Duration.ofMinutes(60));
        manager.createNewTask(task);

        Set<Task> treeSetOfTasks = manager.getPrioritizedTasks();
        List<Task> expected = new ArrayList<>();
        expected.add(subtaskNumberOne);
        expected.add(subtaskNumberTwo);
        expected.add(task);
        expected.add(taskNumberOne);
        assertEquals(expected.toString(), treeSetOfTasks.toString(), "Приоритет задач не сохраняется.");
    }

    @Test
    void checkTheStartTimeAndEndTimeAndDuration() {
        Epic epic = new Epic(null, "Epic",
                "checkTheStartTimeAndEndTimeAndDuration", null,
                null, null);
        manager.createNewEpic(epic);

        Subtask subtaskNumber1 = new Subtask(null, "Subtask1",
                "checkTheStartTimeAndEndTimeAndDuration", TaskStatus.IN_PROGRESS,
                of(2024, 4, 15, 10, 0),
                Duration.ofMinutes(20), epic.getId());
        manager.createNewSubtask(subtaskNumber1);

        Subtask subtaskNumber2 = new Subtask(null, "Subtask2",
                "checkTheStartTimeAndEndTimeAndDuration", TaskStatus.IN_PROGRESS,
                of(2024, 5, 8, 15, 0),
                Duration.ofMinutes(60), epic.getId());
        manager.createNewSubtask(subtaskNumber2);

        Subtask subtaskNumber3 = new Subtask(null, "Subtask3",
                "checkTheStartTimeAndEndTimeAndDuration", TaskStatus.IN_PROGRESS,
                of(2024, 7, 28, 14, 0),
                Duration.ofMinutes(60), epic.getId());
        manager.createNewSubtask(subtaskNumber3);

        LocalDateTime start = of(2024, 4, 15, 10, 0);
        LocalDateTime finish = of(2024, 7, 28, 15, 0);
        Duration duration = Duration.between(start, finish);

        assertEquals(epic.getStartTime(), of(2024, 4, 15, 10, 0),
                "Время начала эпика не совпадает.");
        assertEquals(epic.getEndTime(), of(2024, 7, 28, 15, 0),
                "Время окончания эпика не совпадает.");
        assertEquals(epic.getDuration(), duration, "Продолжительность эпика не совпадает.");

    }

}