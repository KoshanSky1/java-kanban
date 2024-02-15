package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.model.TaskStatus;
import com.practicum.kanban.server.HttpTaskServer;
import com.practicum.kanban.service.InMemoryTaskManager;
import com.practicum.kanban.service.Managers;
import com.practicum.kanban.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {

    private HttpTaskServer taskServer;

    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private Task taskNumberOne;
    private Epic epicNumberOne;
    private Subtask subtaskNumberOne;
    private Subtask subtaskNumberTwo;


    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();

        taskServer = new HttpTaskServer(taskManager);

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

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        taskServer.stop();
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество задач");
        assertEquals(taskNumberOne, actual.get(0), "Задачи не совпадают");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(taskNumberOne, actual, "Задачи не совпадают");
    }

    @Test
    void postNewTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");

        Task taskNumberTwo = new Task(null, "Task №2",
                "description of Task №2", TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 10, 11, 0),
                Duration.ofMinutes(60));
        String json = gson.toJson(taskNumberTwo);

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task savedTask = taskManager.getTaskById(5);

        assertNotNull(taskNumberTwo, "Задача не найдена.");
        taskNumberTwo.setId(5);
        assertEquals(taskNumberTwo, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(taskNumberTwo, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void postUpdatedTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");

        Task updatedTask = new Task(1, "Upd Task №1",
                "Upd description of Task №1", TaskStatus.NEW,
                null,
                Duration.ofMinutes(10));
        String json = gson.toJson(updatedTask);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final Task updTask = taskManager.getTaskById(1);
        assertNotNull(updTask, "Задача не найдена.");
        assertEquals(updTask, updatedTask, "Задача не обновилась");
        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, tasks.size(), "Изменился размер списка задач.");
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Список задач не пустой");
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Размер списка задач не изменился");
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual, "Эпики не возвращаются");
        assertEquals(1, actual.size(), "Неверное количество эпиков");
        assertEquals(epicNumberOne, actual.get(0), "Эпики не совпадают");

    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), epicType);

        assertNotNull(actual, "Задачи не возвращаются");
        assertEquals(epicNumberOne, actual, "Задачи не совпадают");

    }

    @Test
    void postNewEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        Epic epicNumberTwo = new Epic(null, "Epic №2",
                "description of Epic №2", TaskStatus.NEW,
                null,
                null);
        String json = gson.toJson(epicNumberTwo);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic savedEpic = taskManager.getEpicById(5);

        assertNotNull(epicNumberTwo, "Эпик не найден");
        epicNumberTwo.setId(5);
        assertEquals(epicNumberTwo, savedEpic, "Эпики не совпадают.");

        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(epicNumberTwo, epics.get(1), "Эпики не совпадают.");
    }

    @Test
    void postUpdatedEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        Epic updatedEpic = new Epic(2, "Upd Epic №1",
                "Upd description of Epic №1", TaskStatus.NEW,
                null,
                null);
        String json = gson.toJson(updatedEpic);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final Epic updEpic = taskManager.getEpicById(2);
        assertNotNull(updEpic, "Эпик не найден.");
        updatedEpic.setStartTime(subtaskNumberOne.getStartTime());
        updatedEpic.setStatus(TaskStatus.IN_PROGRESS);
        updatedEpic.setDuration(Duration.ofMinutes(2103850));
        assertEquals(updEpic, updatedEpic, "Эпик не обновился");
        final List<Epic> epics = taskManager.getAllEpics();
        assertEquals(1, epics.size(), "Изменился размер списка эпиков.");
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "Список эпиков не пустой");
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "Список эпиков не пустой");
    }

    @Test
    void getAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);
        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(2, actual.size(), "Неверное количество подзадач");
        assertEquals(subtaskNumberOne, actual.get(0), "Подзадачи не совпадают");
        assertEquals(subtaskNumberTwo, actual.get(1), "Подзадачи не совпадают");

    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<Subtask>() {
        }.getType();
        Subtask actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(subtaskNumberOne, actual, "Задачи не совпадают");
    }

    @Test
    void postNewSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        Subtask subtaskNumberThree = new Subtask(null, "Subtask №3",
                "description of Subtask №3", TaskStatus.NEW,
                LocalDateTime.of(2030, 5, 1, 10, 0),
                Duration.ofMinutes(10), epicNumberOne.getId());
        String json = gson.toJson(subtaskNumberThree);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask savedSubtask = taskManager.getSubtaskById(5);

        assertNotNull(subtaskNumberThree, "Подзадача не найдена");
        subtaskNumberThree.setId(5);
        assertEquals(subtaskNumberThree, savedSubtask, "Подзадачи не совпадают.");

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void postUpdatedSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        Subtask updatedSubtask = new Subtask(3, "Upd Subtask №3",
                "Upd description of Subtask №3", TaskStatus.NEW,
                LocalDateTime.of(2030, 5, 1, 10, 0),
                Duration.ofMinutes(100), epicNumberOne.getId());
        String json = gson.toJson(updatedSubtask);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final Subtask updSubtask = taskManager.getSubtaskById(3);
        assertNotNull(updatedSubtask, "Подзадача не найдена.");
        assertEquals(updSubtask, updatedSubtask, "Подзадача не обновилась");
        final List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size(), "Изменился размер списка подзадач.");
    }

    @Test
    void deleteAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(0, subtasks.size(), "Список подзадач не пустой");

    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Список подзадач не совпадает");

    }

    @Test
    void getSubtaskByEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);
        assertNotNull(actual, "Подзадачи не возвращаются");
        assertEquals(2, actual.size(), "Неверное количество подзадач");
        assertEquals(subtaskNumberOne, actual.get(0), "Подзадачи не совпадают");
        assertEquals(subtaskNumberTwo, actual.get(1), "Подзадачи не совпадают");

    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        taskManager.getTaskById(taskNumberOne.getId());
        taskManager.getEpicById(epicNumberOne.getId());
        taskManager.getSubtaskById(subtaskNumberOne.getId());
        taskManager.getSubtaskById(subtaskNumberTwo.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        List<Task> expected = new ArrayList<>();
        expected.add(taskNumberOne);
        expected.add(epicNumberOne);
        expected.add(subtaskNumberOne);
        expected.add(subtaskNumberTwo);
        assertEquals(actual.get(0).getId(), expected.get(0).getId(), "История различается");
        assertEquals(actual.get(1).getId(), expected.get(1).getId(), "История различается");
        assertEquals(actual.get(2).getId(), expected.get(2).getId(), "История различается");
        assertEquals(actual.get(3).getId(), expected.get(3).getId(), "История различается");
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        List<Task> expected = new ArrayList<>();
        expected.add(taskNumberOne);
        expected.add(subtaskNumberOne);
        expected.add(subtaskNumberTwo);
        assertEquals(actual.get(0).getId(), expected.get(1).getId(), "Приоритет задач не сохраняется");
        assertEquals(actual.get(1).getId(), expected.get(2).getId(), "Приоритет задач не сохраняется");
        assertEquals(actual.get(2).getId(), expected.get(0).getId(), "Приоритет задач не сохраняется");
    }
}