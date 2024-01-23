package com.practicum.kanban.exception.service;
import com.practicum.kanban.exception.exception.ManagerSaveException;
import com.practicum.kanban.exception.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.practicum.kanban.exception.model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path file;

    private static final String TITLE = "id,type,name,status,description,epic";

    public FileBackedTasksManager(Path file) {

        this.file = file;

    }

    public static void main(String[] args) throws IOException {

        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");

        InMemoryTaskManager manager = new FileBackedTasksManager(fileKanban.toPath());

        Task taskNumberOne = new Task(null, "Купить корм для котиков", "Grandorf с курицей - 18 баночек", TaskStatus.NEW);
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
        "на 10 февраля", TaskStatus.NEW, epicNumberOne.getId());
        manager.createNewSubtask(subtaskNumberTwo);


        Epic EpicNumberTwo = new Epic(null, "Навести порядок в шкафу в ванной",
        "+ пополнить запасы", TaskStatus.NEW);
        manager.createNewEpic(EpicNumberTwo);

        manager.deleteSubtaskById(4);

        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);

        FileBackedTasksManager fileManager = loadFromFile(new File("fileKanban"));

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

            fileWriter.append(" ");
            fileWriter.append("\n");

            fileWriter.append(historyToString(historyManager));


        } catch (IOException exception) {

            throw new ManagerSaveException(exception.getMessage());

        }

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

    private static Task fromString(String value) {

        Task task = null;
        String description = null;

        String[] lineContents = value.split(",");
        Integer id = Integer.parseInt(lineContents[0]);
        TaskType type = TaskType.valueOf(lineContents[1]);
        String name = lineContents[2];
        TaskStatus status = TaskStatus.valueOf(lineContents[3]);

        if (lineContents[4] != null && !lineContents[4].isBlank()) {

            description = lineContents[4];
        }

        if (type == TASK) {

            task = new Task(id, name, description, status);

        } else if (type == EPIC) {

            task = new Epic(id, name, description, status);

        } else if (type == SUBTASK) {

            Integer epicID = Integer.valueOf(lineContents[5]);
            task = new Subtask(id, name, description, status, epicID);

        }

        return task;

    }

    private static List<Integer> historyFromString(String value) {

        List<Integer> history = new ArrayList<>();
        String[] split = value.split(",");

        for (String number : split) {

            Integer id = Integer.valueOf(number);
            history.add(id);

        }

        return history;

    }

    static FileBackedTasksManager loadFromFile(File file) throws IOException {

        try {

            String readString = Files.readString(Path.of("C:/Users/StrateX/dev/java-kanban/kanban.csv"));

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        FileReader reader = new FileReader("C:/Users/StrateX/dev/java-kanban/kanban.csv");
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {

            String line = br.readLine();
            System.out.println(line);

            if (line.contains("description")) {
                continue;
            } else if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                fromString(line);
            } else if (line.isEmpty() || line.isBlank()) {
                continue;
            } else {
                historyFromString(line);
            }

        }

        br.close();
        return new FileBackedTasksManager(file.toPath());

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

}