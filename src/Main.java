import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);
        Epic epic1 = new Epic("Эпик 1", "Эпик с тремя подзадачами");
        Epic epic2 = new Epic("Эпик 2", "Эпик без подзадач");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW, epic1);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic1);

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createEpic(epic1);
        tm.createEpic(epic2);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        tm.createSubtask(subtask3);

        printHistory(tm);

        tm.getTaskById(0);
        tm.getTaskById(1);
        tm.getTaskById(0);
        tm.getEpicById(2);
        tm.getEpicById(3);
        tm.getSubtaskById(4);
        tm.getTaskById(1);
        tm.getEpicById(2);
        tm.getEpicById(3);
        tm.getSubtaskById(5);
        tm.getSubtaskById(6);
        tm.getSubtaskById(4);
        tm.getSubtaskById(5);
        tm.getSubtaskById(6);

        printHistory(tm);

        tm.deleteTaskById(0);
        printHistory(tm);

        tm.deleteEpicById(2);
        printHistory(tm);
    }

    static void printTasks(TaskManager tm){
        System.out.println("////////////////");
        System.out.println("Список Эпиков - " + tm.getEpics().toString());
        System.out.println("Список задач - " + tm.getTasks().toString());
        System.out.println("Список подзадач - " + tm.getSubtasks().toString());
    }

    static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
