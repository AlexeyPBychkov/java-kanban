import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = new TaskManager();
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);
        Epic epic1 = new Epic("Эпик 1", "Эпик с двумя подзадачами");
        Epic epic2 = new Epic("Эпик 2", "Эпик с одной подзадачей");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW, epic1);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic2);

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createEpic(epic1);
        tm.createEpic(epic2);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        tm.createSubtask(subtask3);

        printTasks(tm);

        task1.setStatus(TaskStatus.DONE);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(task1);
        tm.updateTask(task2);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask1);
        tm.updateSubtask(subtask2);
        tm.updateSubtask(subtask3);

        printTasks(tm);

        tm.deleteTaskById(0);
        tm.deleteEpicById(2);

        printTasks(tm);
    }

    static void printTasks(TaskManager tm){
        System.out.println("////////////////");
        System.out.println("Список Эпиков - " + tm.getEpics().toString());
        System.out.println("Список задач - " + tm.getTasks().toString());
        System.out.println("Список подзадач - " + tm.getSubtasks().toString());
    }
}
