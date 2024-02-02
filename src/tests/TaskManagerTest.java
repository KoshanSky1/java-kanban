package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.service.TaskManager;
import org.testng.annotations.Test;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    Task taskNumberOne;
    Epic epicNumberOne;
    Subtask subtaskNumberOne;
    Subtask subtaskNumberTwo;
    Epic epicNumberTwo;

    protected void addTestTasks() {
        taskNumberOne = new Task(1, "Task №1",
                "description of Task №1", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));
        manager.createNewTask(taskNumberOne);

        epicNumberOne = new Epic(2, "Epic №1",
                "description of Epic №1", TaskStatus.NEW,
                null, null);
        manager.createNewEpic(epicNumberOne);

        subtaskNumberOne = new Subtask(3, "Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);

        subtaskNumberTwo = new Subtask(4, "Subtask №2",
                "descriptio`n of Subtask №2", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(60), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        epicNumberTwo = new Epic(null, "Epic №1",
                "description of Epic №1", TaskStatus.DONE,
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
        assertEquals(savedEpic.getStartTime(), LocalDateTime.of(2023, 3, 8,
                10, 0), "Время начала не совпадает");
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
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
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
        manager.deleteAllTasks(); //?
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
        manager.deleteAllEpics(); //?
        List<Epic> expected = manager.getAllEpics();
        assertTrue(expected.isEmpty(), "Список эпиков пуст.");
    }

    @Test
    void getAllSubtasks() {
        List<Subtask> actual = new ArrayList<>();
        actual.add(subtaskNumberOne);
        List<Subtask> expected = manager.getAllSubtasks();
        assertNotNull(expected, "Подзадачи не возвращаются.");
        assertEquals(1, expected.size(), "Неверное количество подзадач.");
        assertEquals(expected, actual, "Списки не совпадают.");
    }

    @Test
    void getAllSubtasksWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks(); //?
        List<Subtask> expected = manager.getAllSubtasks();
        assertTrue(expected.isEmpty(), "Список подзадач пуст.");
    }

    @Test
    void deleteAllTasks() {
        manager.createNewTask(taskNumberOne);
        ;
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
        List<Task> expected = manager.getAllTasks();
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
        assertEquals(tasksUpdate.get(0), taskNumberOne, "Изменился размер списка задач.");
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
    void updateEpicWithNonexistentId() {
        Epic epicUpdate = new Epic(77, "Upd Epic №1",
                "Upd description of Epic №1", null,
                null, null);

        manager.updateTask(epicUpdate);
        final Task updatedTask = manager.getTaskById(77);
        assertNotNull(updatedTask, "Эпик найден.");
        final List<Epic> tasksUpdate = manager.getAllEpics();
        assertEquals(tasksUpdate.size(), 2, "Изменился размер списка задач.");
    }

    @Test
    void updateSubtask() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask updatedSubtask = new Subtask(3, "Upd Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        manager.updateSubtask(updatedSubtask);
        assertNotNull(updatedSubtask, "Подзадача не найдена.");
        assertEquals(manager.getSubtaskById(3), updatedSubtask, "Подзадача не обновилась");
        List<Subtask> subtasksUpd = manager.getAllSubtasks();
    }

    @Test
    void updateSubtaskWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask subtaskUpdate = new Subtask(3, "Upd Subtask №1",
                "Upd description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        List<Subtask> subtasksUpd = manager.getAllSubtasks();

        final Subtask updatedSubtask = manager.getSubtaskById(3);
        assertNotNull(subtaskUpdate, "Подзадача найдена.");
        assertEquals(subtasks.size(), subtasksUpd.size(), "Изменился размер списка подзадач.");
    }

    @Test
    void updateSubtaskWithNonexistentId() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        Subtask subtaskUpdate = new Subtask(77, "Upd Subtask №1",
                "Upd description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());

        manager.updateSubtask(subtaskUpdate);
        List<Subtask> subtasksUpd = manager.getAllSubtasks();
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
        manager.createNewTask(taskNumberOne);
        List<Task> tasks = manager.getAllTasks();
        manager.deleteTaskById(77);
        List<Task> tasksDel = manager.getAllTasks();
        assertEquals(tasks, tasksDel, "Списки задач различаются.");
    }

    @Test
    void deleteEpicById() {
        List<Epic> epics = manager.getAllEpics();
        List<Subtask> subtasks = manager.getAllSubtasks();
        manager.deleteEpicById(2);
        List<Epic> epicsDel = manager.getAllEpics();
        assertEquals(2, epics.size(), "Эпик не удалился");
        List<Subtask> subtasksDel = manager.getAllSubtasks();
        assertEquals(2, epics.size(), "Подзадачи не удалились");
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
        assertEquals(0, subtaskssDel.size(),
                "Подзадачи не могут существовать с пустым списком эпиков.");
    }

    @Test
    void deleteSubtaskById() {
        List<Subtask> subtasks = manager.getAllSubtasks();
        manager.deleteSubtaskById(3);
        List<Subtask> subtasksDel = manager.getAllSubtasks();
        assertEquals(0, subtasksDel.size(), "Подзадачи не удалились");
    }

    @Test
    void getSubtasksByEpic() {
        List<Subtask> actual = new ArrayList<>();
        actual.add(subtaskNumberOne);
        List<Subtask> expected = manager.getSubtasksByEpic(2);
        assertEquals(actual, expected, "Списки подзадач различаются.");
    }

    @Test
    void getSubtasksByEpicWithAnEmptySubtaskList() {
        manager.deleteAllSubtasks();
        List<Subtask> expected =
                manager.getSubtasksByEpic(2);
        assertEquals(0, expected.size(), "Список подзадач пустой.");
    }

    @Test
    void getHistory() {
        manager.getTaskById(taskNumberOne.getId());
        manager.getEpicById(epicNumberOne.getId());
        manager.getSubtaskById(subtaskNumberOne.getId());
        List<Task> actual = new ArrayList<>();
        actual.add(taskNumberOne);
        actual.add(epicNumberOne);
        actual.add(subtaskNumberOne);
        List<Task> expected = manager.getHistory();
        assertEquals(actual, expected, "История различается");
    }

    @Test
    void getHistoryWithAnEmptySubtaskList() {
        List<Task> expected = manager.getHistory();
        assertEquals(0, expected.size(), "История пуста.");
    }
}