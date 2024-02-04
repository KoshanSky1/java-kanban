package com.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(Integer id, String name, String description, TaskStatus
            status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        String result = id + "," + getType() + "," + name + "," + status;
        if (description != null) {
            result = result + "," + description;
        }
        if (startTime != null) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
            String formatStartTime = startTime.format(formatter);
            result = result + "," + formatStartTime;
        } else {
            result = result + "," + "null";
        }
        if (duration != null) {
            result = result + "," + duration.toMinutes();
        } else {
            result = result + ",null";
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

}