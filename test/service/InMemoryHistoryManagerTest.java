package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryHistoryManagerTest {

    TaskManager tm;
    HistoryManager hm;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void prepareTest() {
        tm = Managers.getDefault();
        task1 = new Task("Title1", "Description", TaskStatus.NEW);
        task1.setId(1);
        task2 = new Task("Title2", "Description", TaskStatus.NEW);
        task2.setId(2);
        task3 = new Task("Title3", "Description", TaskStatus.NEW);
        task3.setId(3);
        hm = new InMemoryHistoryManager();
    }

    @Test
    void getHistoryListTest() {
        tm.createTask(task1);
        Assertions.assertEquals(tm.getHistory().size(), 0);
        tm.getTaskById(0);
        Assertions.assertEquals(tm.getHistory().size(), 1);
    }

    @Test
    void addTest() {
        Assertions.assertEquals(hm.getHistoryList().size(), 0);
        hm.add(task1);
        Assertions.assertEquals(hm.getHistoryList().size(), 1);
    }

    @Test
    void deleteTaskHistoryTest() {
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        Assertions.assertEquals(hm.getHistoryList(), List.of(task1, task2, task3),
                "Проверка истории перед удалением");
        hm.remove(2);
        Assertions.assertEquals(hm.getHistoryList(), List.of(task1, task3),
                "Проверка истории после удаления");
    }
}