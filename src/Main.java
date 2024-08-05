import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = Managers.loadFromFile(new File("task.csv"));
        Task task1 = new Task("Задача 11", "Описание задачи 11", TaskStatus.NEW);

        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(180));
        tm.createTask(task1);

        printAllTasks(tm);
    }

    private static void printTasks(TaskManager tm) {
        System.out.println("////////////////");
        System.out.println("Список Эпиков - " + tm.getEpics().toString());
        System.out.println("Список задач - " + tm.getTasks().toString());
        System.out.println("Список подзадач - " + tm.getSubtasks().toString());
    }

    private static void printHistory(TaskManager manager) {
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
