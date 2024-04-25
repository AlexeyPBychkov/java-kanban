package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int seqId = -1;
    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        task.setId(getCurrentId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());
        return epicList;
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getCurrentId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Subtask> subtasksFromEpic = epics.get(id).getSubtasks();
        for(Subtask subtask : subtasksFromEpic) {
            int keyForDelete = -1;
            for(int key : subtasks.keySet()) {
                if(subtask.equals(subtasks.get(key))){
                    keyForDelete = key;
                }
            }
            subtasks.remove(keyForDelete);
        }
        epics.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int key : epics.keySet()) {
            epics.get(key).setStatus(TaskStatus.NEW);
            epics.get(key).getSubtasks().clear();
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(getCurrentId());
        subtasks.put(subtask.getId(), subtask);
        subtask.getParentEpic().addSubtask(subtask);
        updateEpicStatus(subtask.getParentEpic());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = subtask.getParentEpic();
        subtasks.replace(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    @Override
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

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        return epics.get(epicId).getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryList();
    }

    private int getCurrentId() {
        seqId++;
        return seqId;
    }
}
