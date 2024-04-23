package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

@DisplayName("Task")
public class TaskTest {

    @Test
    void shouldTaskEqualEachOtherIfIdEquals(){
        TaskManager tm = Managers.getDefault();
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        tm.createTask(task);
        Task taskFromManager = tm.getTaskById(0);
        Assertions.assertTrue(task.equals(taskFromManager), "Таски должны быть равны друг другу");
    }
}
