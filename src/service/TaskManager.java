package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int seqId = -1;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public HashMap<Integer, Task> getTasks() {
        return this.tasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(getCurrentId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(getCurrentId());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int key : epics.keySet()) {
            epics.get(key).setStatus(TaskStatus.NEW);
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(getCurrentId());
        subtasks.put(subtask.getId(), subtask);
        subtask.getParentEpic().addSubtask(subtask);
        updateEpicStatus(subtask.getParentEpic());
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = subtask.getParentEpic();
        subtasks.replace(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    public void deleteSubtaskById(int id) {
        Epic epic = subtasks.get(id).getParentEpic();
        epic.removeSubtask(subtasks.get(id));
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        int newCount = 0;
        int progressCount = 0;
        int doneCount = 0;
        if(epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        for (Subtask subtask : epic.getSubtasks()) {
            switch (subtask.getStatus()) {
                case NEW:
                    newCount++;
                    break;
                case IN_PROGRESS:
                    progressCount++;
                    break;
                case DONE:
                    doneCount++;
                    break;
            }
        }
        if(newCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        if(doneCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.DONE);
            return;
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }

    private int getCurrentId() {
        seqId++;
        return seqId;
    }
}
