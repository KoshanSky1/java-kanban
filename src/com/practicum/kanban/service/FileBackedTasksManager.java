package com.practicum.kanban.service;

import com.practicum.kanban.exception.ManagerSaveException;
import com.practicum.kanban.model.*;
import com.practicum.kanban.model.TaskType;

import static com.practicum.kanban.model.TaskType.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path file;
    private static final String TITLE = "id,type,name,status,description,epic";

    public FileBackedTasksManager(Path file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        InMemoryTaskManager manager = new FileBackedTasksManager(fileKanban.toPath());

        Task taskNumberOne = new Task(1, "Task №1", "description of Task №1", TaskStatus.NEW,
                null, null);
        manager.createNewTask(taskNumberOne);

        Epic epicNumberOne = new Epic(2, "Epic №1",
                "description of Epic №1", TaskStatus.NEW,
                null, null);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(3, "Subtask №1",
                "description of Subtask №1", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 10, 0),
                Duration.ofMinutes(120), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);

        Subtask subtaskNumberTwo = new Subtask(4, "Subtask №2",
                "description of Subtask №2", TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2023, 3, 8, 19, 0),
                Duration.ofMinutes(60), epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        Epic epicNumberTwo = new Epic(null, "Epic №2",
                "description of Epic №2", TaskStatus.DONE,
                null, null);
        manager.createNewEpic(epicNumberTwo);

        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);

        loadFromFile(new File("C:/Users/StrateX/dev/java-kanban/kanban.csv"));
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter fileWriter = Files.newBufferedWriter(file)) {
            fileWriter.append(TITLE);
            fileWriter.append("\n");

            for (Task task : allTasks.values()) {
                String taskString = task.toString();
                fileWriter.append(taskString);
                fileWriter.append("\n");
            }

            for (Task task : allEpics.values()) {
                String taskString = task.toString();
                fileWriter.append(taskString);
                fileWriter.append("\n");
            }

            for (Task task : allSubtasks.values()) {
                String taskString = task.toString();
                fileWriter.append(taskString);
                fileWriter.append("\n");
            }

            fileWriter.append("\n");
            fileWriter.append(historyToString(historyManager));

        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }

    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updateTask = super.updateTask(task);
        save();
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updateEpic = super.updateEpic(epic);
        save();
        return updateEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updateSubtask = super.updateSubtask(subtask);
        save();
        return updateSubtask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file.toPath());

        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine(); //Skip header

            while (br.ready()) {
                line = br.readLine();

                if (!line.isEmpty() && !line.isBlank()) {
                    manager.fromString(line);
                } else {
                    break;
                }
            }

            line = br.readLine();
            manager.restoreHistory(historyFromString(line));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return manager;
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        for (Task task : history) {
            int id = task.getId();
            String index = Integer.toString(id);
            sb.append(index).append(",");
        }

        int index = sb.length();
        if (sb.length() >= 1) {
            sb.delete(index - 1, index);
        }
        return sb.toString();
    }

    private Task fromString(String value) {
        Task task = null;
        String[] lineContents = value.split(",");
        Integer id = Integer.parseInt(lineContents[0]);
        TaskType type = TaskType.valueOf(lineContents[1]);
        String name = lineContents[2];
        TaskStatus status = TaskStatus.valueOf(lineContents[3]);
        String description = lineContents[4];
        LocalDateTime startTime;
        Duration duration;

        if (!lineContents[5].equals("null")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");
            startTime = LocalDateTime.parse(lineContents[5], formatter);
        } else {
            startTime = null;
        }

        if (!lineContents[6].equals("null")) {
            duration = Duration.ofMinutes(Long.parseLong(lineContents[6]));
        } else {
            duration = null;
        }

        if (super.id < id) {
            super.id = id;
        }

        if (type == TASK) {
            task = new Task(id, name, description, status, startTime, duration);
            allTasks.put(id, task);
        } else if (type == EPIC) {
            task = new Epic(id, name, description, status, startTime, duration);
            allEpics.put(id, (Epic) task);
        } else if (type == SUBTASK) {
            Integer epicID = Integer.valueOf(lineContents[7]);
            task = new Subtask(id, name, description, status, startTime, duration, epicID);
            allSubtasks.put(id, (Subtask) task);
            Epic epic = allEpics.get(((Subtask) task).getEpicId());
            List<Integer> subtasks = epic.getSubtasksId();
            subtasks.add(task.getId());
        }
        return task;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();

        if (value != null && !value.isEmpty()) {
            String[] split = value.split(",");
            for (String number : split) {
                Integer id = Integer.valueOf(number);
                history.add(id);
            }
        }
        return history;
    }

    private void restoreHistory(List<Integer> arraylist) {
        for (Integer id : arraylist) {
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