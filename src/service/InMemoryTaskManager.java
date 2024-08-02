package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManager;
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int seqId = -1;
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer idx : tasks.keySet()) {
            historyManager.remove(idx);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        if (isNewTaskHasNoIntersection(task)) {
            task.setId(getCurrentId());
            tasks.put(task.getId(), task);
            addPrioritizedTask(task);
        } else {
            System.out.println("Задача не создана. " +
                    "Новая задача пересекается по времени с имеющейся");
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Integer idx : epics.keySet()) {
            historyManager.remove(idx);
        }
        epics.clear();
        for (Integer idx : subtasks.keySet()) {
            historyManager.remove(idx);
        }
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
        addPrioritizedTask(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.replace(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Subtask> subtasksFromEpic = epics.get(id).getSubtasks();
        for (Subtask subtask : subtasksFromEpic) {
            int keyForDelete = -1;
            for (int key : subtasks.keySet()) {
                if (subtask.equals(subtasks.get(key))) {
                    keyForDelete = key;
                }
            }
            subtasks.remove(keyForDelete);
            historyManager.remove(keyForDelete);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer idx : subtasks.keySet()) {
            historyManager.remove(idx);
        }
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
        if (isNewTaskHasNoIntersection(subtask)) {
            subtask.setId(getCurrentId());
            subtasks.put(subtask.getId(), subtask);
            subtask.getParentEpic().addSubtask(subtask);
            updateEpicStatus(subtask.getParentEpic());
            addPrioritizedTask(subtask);
        } else {
            System.out.println("Задача не создана. " +
                    "Новая задача пересекается по времени с имеющейся");
        }
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
        historyManager.remove(id);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        int newCount = 0;
        int progressCount = 0;
        int doneCount = 0;
        if (epic.getSubtasks().isEmpty()) {
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
        if (newCount == epic.getSubtasks().size()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        if (doneCount == epic.getSubtasks().size()) {
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

    public void addPrioritizedTask(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    protected boolean isTaskIntersectOtherTask(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null || t1.getDuration() == null
                || t2.getDuration() == null) {
            return false;
        } else {
            if (t1.getStartTime().isAfter(t2.getStartTime())) {
                Task t = t1;
                t1 = t2;
                t2 = t;
            }
            if (t1.getEndTime().isBefore(t2.getStartTime())) {
                return false;
            }
            return true;
        }
    }

    protected boolean isNewTaskHasNoIntersection(Task task) {
        return getPrioritizedTasks().stream()
                .filter(task1 -> isTaskIntersectOtherTask(task1, task))
                .collect(Collectors.toList()).isEmpty();
    }
}
