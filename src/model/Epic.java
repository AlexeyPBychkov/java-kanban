package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        int idx = -1;
        for (int i = 0; i < subtasks.size(); i++) {
            if(subtasks.get(i).equals(subtask)) {
                idx = i;
            }
        }
        if(idx >= 0) {
            subtasks.remove(idx);
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

}
