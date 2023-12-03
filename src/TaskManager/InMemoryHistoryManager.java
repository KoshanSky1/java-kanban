package TaskManager;
import Models.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyOfViewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        historyOfViewedTasks.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {   // история просмотров
        if (historyOfViewedTasks.size() > 10) {
            int tasksToDelete = historyOfViewedTasks.size() - 10;
            for (int index = 0; index < tasksToDelete; index++) {
                Task remove = historyOfViewedTasks.remove(index);
            }
        }
        System.out.println(historyOfViewedTasks);
        return historyOfViewedTasks;
    }

    public ArrayList<Task> getHistoryOfViewedTasks() {
        return historyOfViewedTasks;
    }

    public void setHistoryOfViewedTasks(ArrayList<Task> historyOfViewedTasks) {
        this.historyOfViewedTasks = historyOfViewedTasks;
    }
}
