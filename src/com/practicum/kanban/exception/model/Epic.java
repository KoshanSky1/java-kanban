package com.practicum.kanban.exception.model;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasksId = new ArrayList<>();
    public Epic(Integer id, String name, String description, TaskStatus status) {

        super(id, name, description, status);

    }
    @Override
    public TaskType getType() {

        return TaskType.EPIC;

    }

    public List<Integer> getSubtasksId() {

        return subtasksId;

    }

    public void setSubtasksId(List<Integer> subtasksId) {

        this.subtasksId = subtasksId;

    }

}