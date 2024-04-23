package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    TaskManager tm;
    @BeforeEach
    void init() {
        tm = Managers.getDefault();
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
    }
    @Test
    void getTasksTest() {
        Assertions.assertEquals(tm.getTasks().size(), 2);
    }

    @Test
    void deleteAllTasksTest() {
        tm.deleteAllTasks();
        Assertions.assertEquals(tm.getTasks().size(), 0);
    }

    @Test
    void getTaskByIdTest() {
        Task task = tm.getTaskById(0);
        Assertions.assertEquals(task.getTitle(), "Задача 1");
    }

    @Test
    void createTaskTest() {
        Task task = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW);
        Assertions.assertEquals(tm.getTasks().size(), 2);
        tm.createTask(task);
        Assertions.assertEquals(tm.getTasks().size(), 3);
    }

    @Test
    void updateTaskTest() {
        Task task = tm.getTaskById(0);
        Assertions.assertEquals(task.getTitle(),"Задача 1");
        task.setTitle("NEW Задача 1");
        tm.updateTask(task);
        Assertions.assertEquals(tm.getTasks().get(0).getTitle(), "NEW Задача 1");
    }

    @Test
    void deleteTaskByIdTest() {
        boolean isTaskExist = false;
        for (Task task : tm.getTasks()) {
            if(task.getId() == 0) {
                isTaskExist = true;
            }
        }
        Assertions.assertTrue(isTaskExist);
        isTaskExist = false;
        tm.deleteTaskById(0);
        for (Task task : tm.getTasks()) {
            if(task.getId() == 0) {
                isTaskExist = true;
            }
        }
        Assertions.assertFalse(isTaskExist);
    }

    @Test
    void getEpicsTest() {
        Assertions.assertEquals(tm.getEpics().size(), 2);
    }

    @Test
    void deleteAllEpicsTest() {
        Assertions.assertEquals(tm.getEpics().size(), 2);
        Assertions.assertEquals(tm.getSubtasks().size(), 3);
        tm.deleteAllEpics();
        Assertions.assertEquals(tm.getEpics().size(), 0);
        Assertions.assertEquals(tm.getSubtasks().size(), 0);
    }

    @Test
    void getEpicByIdTest() {
        Assertions.assertEquals(tm.getEpicById(2).getTitle(), "Эпик 1");
    }

    @Test
    void createEpicTest() {
        Assertions.assertEquals(tm.getEpics().size(), 2);
        Epic epic = new Epic("Эпик 3", "Эпик без подзадач");
        tm.createEpic(epic);
        Assertions.assertEquals(tm.getEpics().size(), 3);
    }

    @Test
    void updateEpicTest() {
        Epic epic = tm.getEpicById(2);
        Assertions.assertEquals(tm.getEpicById(2).getTitle(), "Эпик 1");
        epic.setTitle("NEW Эпик 1");
        tm.updateEpic(epic);
        Assertions.assertEquals(tm.getEpicById(2).getTitle(), "NEW Эпик 1");
    }

    @Test
    void deleteEpicByIdTest() {
        boolean isEpicExist = false;
        for (Epic epic : tm.getEpics()) {
            if(epic.getId() == 2) {
                isEpicExist = true;
            }
        }
        Assertions.assertTrue(isEpicExist);
        isEpicExist = false;
        tm.deleteEpicById(2);
        for (Epic epic : tm.getEpics()) {
            if(epic.getId() == 2) {
                isEpicExist = true;
            }
        }
        Assertions.assertFalse(isEpicExist);
    }

    @Test
    void getSubtasksTest() {
        Assertions.assertEquals(tm.getSubtasks().size(), 3);
    }

    @Test
    void deleteAllSubtasksTest() {
        Assertions.assertEquals(tm.getSubtasks().size(), 3);
        for(Epic epic : tm.getEpics()) {
            Assertions.assertTrue(epic.getSubtasks().size() > 0);
        }
        tm.deleteAllSubtasks();
        Assertions.assertEquals(tm.getSubtasks().size(), 0);
        for(Epic epic : tm.getEpics()) {
            Assertions.assertTrue(epic.getSubtasks().size() == 0);
        }
    }

    @Test
    void getSubtaskByIdTest() {
        Assertions.assertEquals(tm.getSubtaskById(4).getTitle(), "Подзадача 1");
    }

    @Test
    void createSubtaskTest() {
        Assertions.assertEquals(tm.getSubtasks().size(), 3);
        Epic epic = new Epic("Эпик 3", "Эпик с одной подзадачей");
        Subtask subtask = new Subtask("Подзадача 4", "Описание подзадачи 4", TaskStatus.NEW, epic);
        tm.createEpic(epic);
        tm.createSubtask(subtask);
        Assertions.assertEquals(tm.getSubtasks().size(), 4);
    }

    @Test
    void updateSubtaskTest() {
        Subtask subtask = tm.getSubtaskById(4);
        Assertions.assertEquals(tm.getSubtaskById(4).getTitle(), "Подзадача 1");
        subtask.setTitle("NEW Подзадача 1");
        tm.updateSubtask(subtask);
        Assertions.assertEquals(tm.getSubtaskById(4).getTitle(), "NEW Подзадача 1");
    }

    @Test
    void deleteSubtaskByIdTest() {
        boolean isSubtaskExist = false;
        for (Subtask subtask : tm.getSubtasks()) {
            if(subtask.getId() == 4) {
                isSubtaskExist = true;
            }
        }
        Assertions.assertTrue(isSubtaskExist);
        isSubtaskExist = false;
        tm.deleteSubtaskById(4);
        for (Subtask subtask : tm.getSubtasks()) {
            if(subtask.getId() == 4) {
                isSubtaskExist = true;
            }
        }
        Assertions.assertFalse(isSubtaskExist);
    }

    @Test
    void getSubtasksByEpicIdTest() {
        Assertions.assertEquals(tm.getSubtasksByEpicId(2).size(), 2);
    }

    @Test
    void getHistoryTest() {
        Assertions.assertEquals(tm.getHistory().size(), 0);
        tm.getTaskById(1);
        Assertions.assertEquals(tm.getHistory().size(), 1);
    }
}