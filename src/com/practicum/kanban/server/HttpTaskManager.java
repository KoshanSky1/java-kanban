package com.practicum.kanban.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.practicum.kanban.model.Epic;
import com.practicum.kanban.model.Subtask;
import com.practicum.kanban.model.Task;
import com.practicum.kanban.service.FileBackedTasksManager;
import com.practicum.kanban.service.Managers;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class HttpTaskManager extends FileBackedTasksManager {
    private final String url = "http://localhost:8078";
    private final KVTaskClient client = new KVTaskClient();
    private final Gson gson = Managers.getGson();

    public HttpTaskManager(Path url) {
        super(url);
    }

    @Override
    public void save() {
        client.put("/tasks", gson.toJson(getAllTasks()));
        client.put("/epics", gson.toJson(getAllEpics()));
        client.put("/subtasks", gson.toJson(getAllSubtasks()));
        client.put("/history", gson.toJson(getHistory()));
    }

    public void load() {
        String responseOfLoadingTasks = client.load("/tasks");
        JsonElement jsonElementTasks = JsonParser.parseString(responseOfLoadingTasks);

        JsonArray jsonArrayOfTasks = jsonElementTasks.getAsJsonArray();
        for (JsonElement jsonElement : jsonArrayOfTasks) {
            Type taskType = new TypeToken<Task>() {
            }.getType();
            Task task = gson.fromJson(jsonElement, taskType);
            int id = task.getId();
            allTasks.put(id, task);
            if (super.id < id) {
                super.id = id;
            }
        }

        String responseOfLoadingEpics = client.load("/epics");
        JsonElement jsonElementEpics = JsonParser.parseString(responseOfLoadingEpics);

        JsonArray jsonArrayOfEpics = jsonElementEpics.getAsJsonArray();
        for (JsonElement jsonElement : jsonArrayOfEpics) {
            Type epicType = new TypeToken<Epic>() {
            }.getType();
            Epic epic = gson.fromJson(jsonElement, epicType);
            int id = epic.getId();
            allEpics.put(id, epic);
            if (super.id < id) {
                super.id = id;
            }
        }

        String responseOfLoadingSubtasks = client.load("/subtasks");
        JsonElement jsonElementSubtasks = JsonParser.parseString(responseOfLoadingSubtasks);

        JsonArray jsonArrayOfSubtasks = jsonElementSubtasks.getAsJsonArray();
        for (JsonElement jsonElement : jsonArrayOfSubtasks) {
            Type subtaskType = new TypeToken<Subtask>() {
            }.getType();
            Subtask subtask = gson.fromJson(jsonElement, subtaskType);
            int id = subtask.getId();
            allSubtasks.put(id, subtask);
            if (super.id < id) {
                super.id = id;
            }
        }

        String responseOfLoadingHistory = client.load("/history");
        JsonElement jsonElementHistory = JsonParser.parseString(responseOfLoadingHistory);

        JsonArray jsonArrayOfHistory = jsonElementHistory.getAsJsonArray();
        for (JsonElement jsonElement : jsonArrayOfHistory) {
            int id = jsonElement.getAsInt();
            if (allTasks.containsKey(id)) {
                historyManager.add(allTasks.get(id));
            } else if (allEpics.containsKey(id)) {
                historyManager.add(allEpics.get(id));
            } else {
                historyManager.add(allSubtasks.get(id));
            }
        }

    }
}