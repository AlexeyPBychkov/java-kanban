package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

@DisplayName("Subtask")
public class SubtaskTest {

    @Test
    void shouldTaskEqualEachOtherIfIdEquals(){
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic("Title", "Description");
        Subtask subtask = new Subtask("Title", "Description", TaskStatus.NEW, epic);
        tm.createTask(subtask);
        Subtask subtaskFromManager = tm.getSubtaskById(0);
        Assertions.assertTrue(subtask.equals(subtask), "Сабтаски должны быть равны друг другу");
    }
}
