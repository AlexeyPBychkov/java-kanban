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
//        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("Задача 11", "Описание задачи 11", TaskStatus.NEW);
//        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);
//        Epic epic1 = new Epic("Эпик 1", "Эпик с тремя подзадачами");
//        Epic epic2 = new Epic("Эпик 2", "Эпик без подзадач");
//        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1);
//        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW, epic1);
//        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic1);
//
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(180));
//        task2.setStartTime(LocalDateTime.now());
//        task2.setDuration(Duration.ofMinutes(140));
//
//        subtask1.setStartTime(LocalDateTime.now());
//        subtask1.setDuration(Duration.ofMinutes(234));
//        subtask2.setStartTime(LocalDateTime.now());
//        subtask2.setDuration(Duration.ofMinutes(45));
//        subtask3.setStartTime(LocalDateTime.now());
//        subtask3.setDuration(Duration.ofMinutes(78));
//
        tm.createTask(task1);
//        tm.createTask(task2);
//        tm.createEpic(epic1);
//        tm.createEpic(epic2);
//        tm.createSubtask(subtask1);
//        tm.createSubtask(subtask2);
//        tm.createSubtask(subtask3);
//
//        printHistory(tm);
//
//        tm.getTaskById(task1.getId());
//        tm.getTaskById(task2.getId());
//        tm.getTaskById(task1.getId());
//        tm.getEpicById(epic1.getId());
//        tm.getEpicById(epic2.getId());
//        tm.getSubtaskById(subtask1.getId());
//        tm.getTaskById(task2.getId());
//        tm.getEpicById(epic1.getId());
//        tm.getEpicById(epic2.getId());
//        tm.getSubtaskById(subtask2.getId());
//        tm.getSubtaskById(subtask3.getId());
//        tm.getSubtaskById(subtask1.getId());
//        tm.getSubtaskById(subtask2.getId());
//        tm.getSubtaskById(subtask3.getId());
//
//        printHistory(tm);

//        tm.deleteTaskById(task1.getId());
//        printHistory(tm);
//
//        tm.deleteEpicById(epic1.getId());
//        printHistory(tm);

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
