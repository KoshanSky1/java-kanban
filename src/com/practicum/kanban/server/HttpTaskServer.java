package com.practicum.kanban.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.service.FileBackedTasksManager;
import com.practicum.kanban.service.Managers;
import com.practicum.kanban.service.TaskManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private HttpServer server;
    private Gson gson;
    private TaskManager taskManager;
    private FileBackedTasksManager manager = new FileBackedTasksManager(Path.of("C:/Users/StrateX/dev/java-kanban/kanban.csv"));

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        try {
            String response;
            String requestPath = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            Endpoint endpoint = getEndpoint(requestPath, requestMethod, query);
            switch (endpoint) {
                case GET_ALL_TASKS:
                    response = gson.toJson(taskManager.getAllTasks());
                    sendText(httpExchange, response);
                    break;
                case GET_TASK_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getTaskById(id));
                            sendText(httpExchange, response);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case POST_TASK:
                    String bodyTask = readText(httpExchange);
                    if (bodyTask.isEmpty()) {
                        System.out.println("Задача не может быть пустой");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    try {
                        Task task = gson.fromJson(bodyTask, Task.class);
                        if (task.getId() == -1) {
                            taskManager.createNewTask(task);
                            System.out.println("Задача успешно добавлена");
                        } else {
                            taskManager.updateTask(task);
                            System.out.println("Задача c id " + task.getId() + " успешно обновлена");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } catch (JsonSyntaxException e) {
                        System.out.println("Получен некорректный JSON");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case DELETE_ALL_TASKS:
                    taskManager.deleteAllTasks();
                    System.out.println("Задачи успешно удалены");
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_TASK_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteTaskById(id);
                            System.out.println("Задача успешно удалена");
                            httpExchange.sendResponseHeaders(200, 0);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case GET_ALL_EPICS:
                    response = gson.toJson(taskManager.getAllEpics());
                    sendText(httpExchange, response);
                    break;
                case GET_EPIC_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getEpicById(id));
                            sendText(httpExchange, response);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case POST_EPIC:
                    String bodyEpic = readText(httpExchange);
                    if (bodyEpic.isEmpty()) {
                        System.out.println("Эпик не может быть пустым");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    try {
                        Epic epic = gson.fromJson(bodyEpic, Epic.class);
                        if (epic.getId() == -1) {
                            taskManager.createNewEpic(epic);
                            System.out.println("Эпик успешно добавлен");
                        } else {
                            taskManager.updateEpic(epic);
                            System.out.println("Эпик успешно обновлён");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } catch (JsonSyntaxException e) {
                        System.out.println("Получен некорректный JSON");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case DELETE_ALL_EPICS:
                    taskManager.deleteAllEpics();
                    System.out.println("Эпики успешно удалены");
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_EPIC_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpicById(id);
                            System.out.println("Эпик успешно удалён");
                            httpExchange.sendResponseHeaders(200, 0);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                case GET_ALL_SUBTASKS:
                    response = gson.toJson(taskManager.getAllSubtasks());
                    sendText(httpExchange, response);
                    break;
                case GET_SUBTASK_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getSubtaskById(id));
                            sendText(httpExchange, response);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case POST_SUBTASK:
                    String bodySubtask = readText(httpExchange);
                    if (bodySubtask.isEmpty()) {
                        System.out.println("Подзадача не может быть пустой");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    try {
                        Subtask subtask = gson.fromJson(bodySubtask, Subtask.class);
                        if (subtask.getId() == -1) {
                            taskManager.createNewSubtask(subtask);
                            System.out.println("Подзадача успешно добавлена");
                        } else {
                            taskManager.updateSubtask(subtask);
                            System.out.println("Подзадача успешно обновлена");
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                    } catch (JsonSyntaxException e) {
                        System.out.println("Получен некорректный JSON");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case DELETE_ALL_SUBTASKS:
                    taskManager.deleteAllSubtasks();
                    System.out.println("Подзадачи успешно удалены");
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_SUBTASK_BY_ID:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            taskManager.deleteSubtaskById(id);
                            System.out.println("Подзадача успешно удалена");
                            httpExchange.sendResponseHeaders(200, 0);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                case GET_SUBTASKS_BY_EPIC:
                    if (Pattern.matches("id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            response = gson.toJson(taskManager.getSubtasksByEpic(id));
                            sendText(httpExchange, response);
                            return;
                        } else {
                            System.out.println("Некорректный id " + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                case GET_HISTORY:
                    response = gson.toJson(taskManager.getHistory());
                    sendText(httpExchange, response);
                    break;
                case GET_PRIORITIZED_TASKS:
                    String prioritizedTasks = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(httpExchange, prioritizedTasks);
                    break;
                case UNKNOWN:
                    System.out.println("Taкого эндпоинта не существует");
                    httpExchange.sendResponseHeaders(404, 0);
                default:
                    System.out.println("Ожидается GET, POST или DELETE запрос. Вы вводите - " + requestMethod + requestPath);
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
        String[] splitStrings = requestPath.split("/");
        if (requestMethod.equals("POST")) {
            switch (splitStrings[2]) {
                case "task" -> {
                    return Endpoint.POST_TASK;
                }
                case "epic" -> {
                    return Endpoint.POST_EPIC;
                }
                case "subtask" -> {
                    return Endpoint.POST_SUBTASK;
                }
                default -> {
                    return Endpoint.UNKNOWN;
                }
            }
        }
        if (splitStrings.length == 2) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }
        if (splitStrings.length == 3 && query == null) {
            switch (splitStrings[2]) {
                case "task" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_ALL_TASKS;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_ALL_TASKS;
                    }
                }
                case "epic" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_ALL_EPICS;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_ALL_EPICS;
                    }
                }
                case "subtask" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_ALL_SUBTASKS;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_ALL_SUBTASKS;
                    }
                }
                case "history" -> {
                    return Endpoint.GET_HISTORY;
                }
                default -> {
                    return Endpoint.UNKNOWN;
                }
            }
        }
        if (splitStrings.length == 3 && query != null) {
            switch (splitStrings[2]) {
                case "task" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_TASK_BY_ID;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_TASK_BY_ID;
                    }
                }
                case "epic" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_EPIC_BY_ID;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_EPIC_BY_ID;
                    }
                }
                case "subtask" -> {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_SUBTASK_BY_ID;
                    } else if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_SUBTASK_BY_ID;
                    }
                }
                default -> {
                    return Endpoint.UNKNOWN;
                }
            }
        }
        if (splitStrings.length == 4) {
            return Endpoint.GET_SUBTASKS_BY_EPIC;
        }
        return Endpoint.UNKNOWN;
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Откройте в браузере http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    enum Endpoint {
        GET_ALL_TASKS, GET_TASK_BY_ID, POST_TASK, DELETE_ALL_TASKS, DELETE_TASK_BY_ID,
        GET_ALL_EPICS, GET_EPIC_BY_ID, POST_EPIC, DELETE_ALL_EPICS, DELETE_EPIC_BY_ID,
        GET_ALL_SUBTASKS, GET_SUBTASK_BY_ID, POST_SUBTASK, DELETE_ALL_SUBTASKS, DELETE_SUBTASK_BY_ID,
        GET_SUBTASKS_BY_EPIC, GET_HISTORY, GET_PRIORITIZED_TASKS, UNKNOWN
    }
}