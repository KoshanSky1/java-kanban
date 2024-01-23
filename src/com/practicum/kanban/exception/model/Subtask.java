package com.practicum.kanban.exception.model;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask(Integer id, String name, String description, TaskStatus status, Integer epicID) {

        super(id, name, description, status);
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