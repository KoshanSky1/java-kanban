package com.practicum.kanban.service;

import com.practicum.kanban.exception.ManagerSaveException;
import com.practicum.kanban.model.*;
import com.practicum.kanban.model.TaskType;

import static com.practicum.kanban.model.TaskType.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

        Task taskNumberOne = new Task(null, "Купить корм для котиков",
                "Grandorf с курицей - 18 баночек", TaskStatus.NEW);
        manager.createNewTask(taskNumberOne);

        Task taskNumberTwo = new Task(null, "Купить eду для синичек",
                "Нежареные! семечки/ корм для попугаев", TaskStatus.NEW);
        manager.createNewTask(taskNumberTwo);

        Epic epicNumberOne = new Epic(null, "Вакцинация кошек", "ежегодная",
                TaskStatus.NEW);
        manager.createNewEpic(epicNumberOne);

        Subtask subtaskNumberOne = new Subtask(null, "Купить препараты", "Нобивак",
                TaskStatus.DONE, epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberOne);

        Subtask subtaskNumberTwo = new Subtask(null, "Записаться к ветеринару",
                "на 10 февраля", TaskStatus.IN_PROGRESS, epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);

        Epic EpicNumberTwo = new Epic(null, "Навести порядок в шкафу в ванной",
                "+ пополнить запасы", TaskStatus.NEW);
        manager.createNewEpic(EpicNumberTwo);

        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);

        loadFromFile(new File("C:/Users/StrateX/dev/java-kanban/kanban.csv"));

    }

    public void save() {
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

    protected static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file.toPath());

        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine(); //Skip header
            while (br.ready()) {
                line = br.readLine();
                System.out.println(line);
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

        if (super.id < id) {
            super.id = id;
        }

        if (type == TASK) {
            task = new Task(id, name, description, status);
            allTasks.put(id, task);
        } else if (type == EPIC) {
            task = new Epic(id, name, description, status);
            allEpics.put(id, (Epic) task);
        } else if (type == SUBTASK) {
            Integer epicID = Integer.valueOf(lineContents[5]);
            task = new Subtask(id, name, description, status, epicID);
            allSubtasks.put(id, (Subtask) task);

            Epic epic = allEpics.get(((Subtask) task).getEpicId());
            List<Integer> subtasks = epic.getSubtasksId();
            subtasks.add(((Subtask) task).getId());
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