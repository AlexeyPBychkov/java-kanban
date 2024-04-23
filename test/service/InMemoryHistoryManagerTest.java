package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void getHistoryListTest() {
        TaskManager tm = Managers.getDefault();
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        tm.createTask(task);
        Assertions.assertEquals(tm.getHistory().size(), 0);
        tm.getTaskById(0);
        Assertions.assertEquals(tm.getHistory().size(), 1);
    }

    @Test
    void addTest() {
        Task task = new Task("Title", "Description", TaskStatus.NEW);
        InMemoryHistoryManager hm = new InMemoryHistoryManager<>();
        Assertions.assertEquals(hm.getHistoryList().size(), 0);
        hm.add(task);
        Assertions.assertEquals(hm.getHistoryList().size(), 1);
    }
}