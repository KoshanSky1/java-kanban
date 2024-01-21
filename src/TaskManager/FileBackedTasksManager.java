package TaskManager;
import Models.Epic;
import Models.Subtask;
import Models.Task;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static TaskManager.TaskTypes.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path file;
    public FileBackedTasksManager(Path file) throws IOException {

        this.file = file;

    }

    public static void main(String[] args) throws IOException {

        File fileKanban = new File("C:/Users/StrateX/dev/java-kanban/kanban.csv");

        InMemoryTaskManager manager = new FileBackedTasksManager(fileKanban.toPath());

        Task taskNumberOne = new Task(manager.assignID(), "Купить корм для котиков",
        "Grandorf с курицей, 18 баночек", TaskStatus.NEW, TASK);

        manager.createNewTask(taskNumberOne);

        Task taskNumberTwo = new Task(manager.assignID(), "Купить eду для синичек",
        "Нежареные! семечки/ корм для попугаев", TaskStatus.NEW, TASK);

        manager.createNewTask(taskNumberTwo);

        Epic EpicNumberOne = new Epic(manager.assignID(), "Вакцинация кошек", "ежегодная",
        TaskStatus.NEW, EPIC);
        manager.createNewEpic(EpicNumberOne);

        Subtask subtaskNumberOne = new Subtask(manager.assignID(), "Купить препараты", "Нобивак",
        TaskStatus.NEW, SUBTASK, EpicNumberOne.getId());

        manager.createNewSubtask(subtaskNumberOne);

        Subtask subtaskNumberTwo = new Subtask(manager.assignID(), "Записаться к ветеринару",
        "на 10 февраля", TaskStatus.IN_PROGRESS, SUBTASK, EpicNumberOne.getId());

        manager.createNewSubtask(subtaskNumberTwo);

        Epic EpicNumberTwo = new Epic(manager.assignID(), "Навести порядок в шкафу в ванной",
        "+ пополнить запасы", TaskStatus.NEW, EPIC);

        manager.createNewEpic(EpicNumberTwo);

        manager.getTaskByID(1);
        manager.getEpicByID(3);
        manager.getSubtaskByID(4);

        FileBackedTasksManager fileManager = loadFromFile(new File("fileKanban"));

    }

    public void save() {

        try (BufferedWriter fileWriter = Files.newBufferedWriter(file)) {

            fileWriter.append("id");
            fileWriter.append(",");
            fileWriter.append("type");
            fileWriter.append(",");
            fileWriter.append("name");
            fileWriter.append(",");
            fileWriter.append("status");
            fileWriter.append(",");
            fileWriter.append("description");
            fileWriter.append(",");
            fileWriter.append("epic");
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


        } catch (ManagerSaveException | IOException exception) {

            System.out.println("Не удалось сохранить данные в файл");

        }

    }

    public static String historyToString(HistoryManager manager) {

        ArrayList<Task> history = manager.getHistory();

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

    public static Task fromString(String value) {

        Task task = null;
        String description = null;

        String[] lineContents = value.split(",");
        Integer id = Integer.parseInt(lineContents[0]);
        TaskTypes type = TaskTypes.valueOf(lineContents[1]);
        String name = lineContents[2];
        TaskStatus status = TaskStatus.valueOf(lineContents[3]);

        if (lineContents[4] != null && !lineContents[4].isBlank()) {

            description = lineContents[4];
        }

        if (type == TASK) {

            task = new Task(id, name, description, status, type);

        } else if (type == EPIC) {

            task = new Epic(id, name, description, status, type);

        } else if (type == SUBTASK) {

            Integer epicID = Integer.valueOf(lineContents[5]);
            task = new Subtask(id, name, description, status, type, epicID);

        }

        return task;

    }

    public static List<Integer> historyFromString(String value) {

        List<Integer> history = new ArrayList<>();
        Task taskByID;
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
            }
            else {
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

    public Task getTaskByID(int id) {

        Task task = super.getTaskByID(id);
        save();
        return task;

    }

    @Override
    public Epic getEpicByID(int id) {

         Epic epic = super.getEpicByID(id);
         save();
         return epic;

    }

    @Override
    public Subtask getSubtaskByID(int id) {

        Subtask subtask = super.getSubtaskByID(id);
        save();
        return subtask;

    }

}