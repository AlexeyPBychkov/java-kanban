package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateEndTime();
    }

    public void removeSubtask(Subtask subtask) {
        int idx = -1;
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).equals(subtask)) {
                idx = i;
            }
        }
        if (idx >= 0) {
            subtasks.remove(idx);
        }
        updateEndTime();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks=" + getSubtasks() +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    private void updateEndTime() {
        for (Subtask subtask : subtasks) {
            if (getStartTime() == null) {
                setStartTime(subtask.getStartTime());
                endTime = subtask.getEndTime();
            } else {
                if (getStartTime().isAfter(subtask.getStartTime())) {
                    setStartTime(subtask.getStartTime());
                }
                if (endTime.isBefore(subtask.getEndTime())) {
                    endTime = subtask.getEndTime();
                }
            }
        }
        setDuration(Duration.between(getStartTime(), getEndTime()));
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
