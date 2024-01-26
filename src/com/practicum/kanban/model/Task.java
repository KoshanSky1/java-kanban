package com.practicum.kanban.model;

import java.util.Objects;

public class Task {

    private Integer id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(Integer id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
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

    @Override
    public String toString() {
        String result = id + "," + getType() + "," + name + "," + status;
        if (description != null) {
            result = result + "," + description;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this.equals(o)) return true;
        if (o == null || getClass().equals(o.getClass())) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

}