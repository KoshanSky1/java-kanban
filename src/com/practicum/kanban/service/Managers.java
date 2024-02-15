package com.practicum.kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.practicum.kanban.adapter.DurationAdapter;
import com.practicum.kanban.adapter.LocalDateTimeAdapter;
import com.practicum.kanban.server.HttpTaskManager;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {
        return new HttpTaskManager(Path.of("http://localhost:8080"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

}