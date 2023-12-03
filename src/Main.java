import Models.Epic;
import Models.Subtask;
import Models.Task;
import TaskManager.Managers;
import TaskManager.TaskManager;
import TaskManager.TaskStatus;

import java.util.Scanner;  // для проверки работы программы

public class Main {
    public static void main(String[] args) {
        checkTheProgramFunctionality();
    }

    static void checkTheProgramFunctionality() { // для проверки работы программы
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = Managers.getDefault();

        Task taskNumberOne = new Task(manager.assignID(), "Купить корм для котиков",
        "Grandorf с курицей, 18 баночек", TaskStatus.NEW);
        Task firstTask = manager.createNewTask(taskNumberOne);

        Task taskNumberTwo = new Task(manager.assignID(),"Купить eду для синичек",
        "Нежареные! семечки/ корм для попугаев",  TaskStatus.NEW);
        Task secondTask =  manager.createNewTask(taskNumberTwo);

        Epic EpicNumberOne = new Epic(manager.assignID(),"Уборка", "Подготовиться к приходу гостей",
        TaskStatus.NEW);
        Epic firstEpic = manager.createNewEpic(EpicNumberOne);

        Subtask subtaskNumberOne = new Subtask(manager.assignID(),"Помыть пол", "", TaskStatus.NEW,
        EpicNumberOne.getId());
        Subtask firstSubtask = manager.createNewSubtask(subtaskNumberOne);

        Subtask subtaskNumberTwo = new Subtask(manager.assignID(), "Протереть пыль", "",
        TaskStatus.IN_PROGRESS, EpicNumberOne.getId());
        Subtask secondSubtask = manager.createNewSubtask(subtaskNumberTwo);

        Epic EpicNumberTwo = new Epic(manager.assignID(), "Накрыть на стол", "Приготовить блюда",
        TaskStatus.NEW);
        Epic secondEpic = manager.createNewEpic(EpicNumberTwo);

        Subtask subtaskNumberThree = new Subtask(manager.assignID(), "Испечь торт", "Медовик",
        TaskStatus.IN_PROGRESS, secondEpic.getId());
        Subtask thirdSubtask = manager.createNewSubtask(subtaskNumberThree);

        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Получить список всех эпиков");
        System.out.println("3 - Получить список всех подзадач");
        System.out.println("4 - Удалить все задачи");
        System.out.println("5 - Удалить все эпики");
        System.out.println("6 - Удалить все подзадачи");
        System.out.println("7 - Получить задачу по ID");
        System.out.println("8 - Получить эпик по ID");
        System.out.println("9 - Получить подзадачу по ID");
        System.out.println("10 - Удалить задачу по ID");
        System.out.println("11 - Удалить эпик по ID");
        System.out.println("12 - Удалить подзадачу по ID");
        System.out.println("13 - Получить список подзадач по ID эпика");
        System.out.println("14 - Обновить подзадачу");
        System.out.println("15 - Получить историю просмотренных задач");

        while (true) {
            int i = 0;
            i = scanner.nextInt();
            switch (i) {
                case 1:
                    System.out.println(manager.getAllTasks());
                    break;
                case 2:
                    System.out.println(manager.getAllEpics());
                    break;
                case 3:
                    System.out.println(manager.getAllSubtasks());
                    break;
                case 4:
                    manager.deleteAllTasks();
                    break;
                case 5:
                    manager.deleteAllEpics();
                    break;
                case 6:
                    manager.deleteAllSubtasks();
                    break;
                case 7:
                    System.out.println(manager.getTaskByID(1));
                    break;
                case 8:
                    System.out.println(manager.getEpicByID(3));
                    break;
                case 9:
                    System.out.println(manager.getSubtaskByID(4));
                    break;
                case 10:
                    manager.deleteTaskByID(2);
                    break;
                case 11:
                    manager.deleteEpicByID(3);
                    break;
                case 12:
                    manager.deleteSubtaskByID(5);
                    break;
                case 13:
                    System.out.println(manager.getSubtasksByEpic(3));
                    break;
                case 14:
                    Subtask subtaskNumber = new Subtask(manager.assignID(), "Испечь пирог", "Манник",
                    TaskStatus.DONE, secondEpic.getId());
                    Subtask thirdSubtaskUpdate = manager.updateSubtask(7, subtaskNumber);
                    break;
                case 15:
                    manager.getHistory();
                    break;
                default:
                    scanner.close();
                    return;
            }
        }
    }
}
