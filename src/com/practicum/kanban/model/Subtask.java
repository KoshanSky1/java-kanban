package com.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, Integer epicID) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicID;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicID) {
        this.epicId = epicID;
    }

    @Override
    public String toString() {
        String result = super.toString();

        result = result + "," + epicId;
        return result;
    }

}