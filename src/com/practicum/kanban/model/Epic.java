package com.practicum.kanban.model;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(Integer id, String name, String description, TaskStatus
            status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}