package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

@DisplayName("Epic")
public class EpicTest {

    @DisplayName("Проверка на добавление сабтаска в эпик")
    @Test
    void shouldAddSubtaskToEpic() {
        Epic epic = new Epic("Title", "Description");
        Subtask subtask = new Subtask("Title", "Description", TaskStatus.NEW, epic);
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(10));
        epic.addSubtask(subtask);
        Subtask subtaskFromEpic = epic.getSubtasks().get(0);
        assertEqualsSubtasks(subtask, subtaskFromEpic, "Сабтаски должны совпадать");
    }

    @DisplayName("Проверка, что эпики равны друг другу если равен ID")
    @Test
    void shouldEpicsEqualEachOtherIfIdEquals(){
        TaskManager tm = Managers.getDefault();
        Epic epic = new Epic("Title", "Description");
        tm.createEpic(epic);
        Epic epicFromManager = tm.getEpicById(0);
        Assertions.assertTrue(epic.equals(epicFromManager), "Эпики должны быть равны друг другу");
    }

    @DisplayName("Проверка удаления сабтаска")
    @Test
    void shouldRemoveSubtask() {
        Epic epic = new Epic("Title", "Description");
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", TaskStatus.NEW, epic);
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(10));
        epic.addSubtask(subtask);
        Assertions.assertEquals(epic.getSubtasks().size(), 1);
        epic.removeSubtask(subtask);
        Assertions.assertEquals(epic.getSubtasks().size(), 0);
    }

    private final void assertEqualsSubtasks(Subtask currentSubtask, Subtask subtaskFromEpic, String message) {
        Assertions.assertEquals(currentSubtask.getTitle(), subtaskFromEpic.getTitle(), message);
        Assertions.assertEquals(currentSubtask.getDescription(), subtaskFromEpic.getDescription(), message);
    }
}
