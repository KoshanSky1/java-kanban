package tests;

import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.service.HistoryManager;
import com.practicum.kanban.service.Managers;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    Task taskNumberOne = new Task(1, "Task №1",
            "description of Task №1", TaskStatus.NEW,
            null,
            Duration.ofMinutes(10));

    Epic epicNumberOne = new Epic(2, "Epic №1",
            "description of Epic №1", TaskStatus.NEW,
            null, null);

    Subtask subtaskNumberOne = new Subtask(3, "Subtask №1",
            "description of Subtask №1", TaskStatus.NEW,
            LocalDateTime.of(2024, 5, 1, 10, 0),
            Duration.ofMinutes(10), epicNumberOne.getId());

    Subtask subtaskNumberTwo = new Subtask(4, "Subtask №3",
            "description of Subtask №3", TaskStatus.IN_PROGRESS,
            LocalDateTime.of(2028, 5, 1, 10, 0),
            Duration.ofMinutes(10), epicNumberOne.getId());

    @Test
    void add() {
        historyManager.add(taskNumberOne);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addTaskAgain() {
        historyManager.add(taskNumberOne);
        historyManager.add(epicNumberOne);
        historyManager.add(taskNumberOne);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "Неправильно меняется размер истории при дублировании.");
        assertEquals(taskNumberOne, history.get(1), "Не изменяется порядок задач в истории");
        assertEquals(epicNumberOne, history.get(0), "Не изменяется порядок задач в истории");
    }

    @Test
    void removeTheFirstElementOfTheList() {
        historyManager.add(epicNumberOne);
        historyManager.add(taskNumberOne);
        historyManager.add(subtaskNumberOne);
        historyManager.remove(epicNumberOne.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(taskNumberOne, history.get(0), "Первый элемент удаляется неправильно.");
        assertEquals(2, history.size(), "Размер истории неверный.");
    }

    @Test
    void removeTheMiddleElementOfTheList() {
        historyManager.add(epicNumberOne);
        historyManager.add(taskNumberOne);
        historyManager.add(subtaskNumberOne);
        historyManager.remove(taskNumberOne.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(subtaskNumberOne, history.get(1), "Элемент из середины удаляется неправильно.");
        assertEquals(2, history.size(), "Размер истории неверный.");
    }

    @Test
    void removeTheLastElementOfTheList() {
        historyManager.add(epicNumberOne);
        historyManager.add(taskNumberOne);
        historyManager.add(subtaskNumberOne);
        historyManager.remove(subtaskNumberOne.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(taskNumberOne, history.get(1),
                "Последний элемент удаляется неправильно.");
        assertEquals(2, history.size(), "Размер истории неверный.");
    }

    @Test
    void getHistory() {
        historyManager.add(taskNumberOne);
        historyManager.add(epicNumberOne);
        historyManager.add(subtaskNumberOne);
        historyManager.add(subtaskNumberTwo);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(4, history.size(), "Размер истории неверный.");
        assertEquals(taskNumberOne, history.get(0), "Не сохраняется порядок добавления задач в историю");
        assertEquals(epicNumberOne, history.get(1), "Не сохраняется порядок добавления задач в историю");
        assertEquals(subtaskNumberOne, history.get(2), "Не сохраняется порядок добавления задач в историю");
        assertEquals(subtaskNumberTwo, history.get(3), "Не сохраняется порядок добавления задач в историю");
    }

    @Test
    void getHistoryIsEmpty() {
        final List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История пустая.");
        assertEquals(0, history.size(), "История пустая.");
    }

    @Test
    void getHistoryWithDuplication() {
        historyManager.add(taskNumberOne);
        historyManager.add(epicNumberOne);
        historyManager.add(subtaskNumberOne);
        historyManager.add(taskNumberOne);
        historyManager.add(subtaskNumberTwo);
        historyManager.add(subtaskNumberOne);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(4, history.size(), "Размер истории неверный.");
        assertEquals(epicNumberOne, history.get(0), "Не сохраняется порядок добавления задач в историю");
        assertEquals(taskNumberOne, history.get(1), "Не сохраняется порядок добавления задач в историю");
        assertEquals(subtaskNumberTwo, history.get(2), "Не сохраняется порядок добавления задач в историю");
        assertEquals(subtaskNumberOne, history.get(3), "Не сохраняется порядок добавления задач в историю");
    }

}