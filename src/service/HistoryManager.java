package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistoryList();

    void add(Task task);

    void remove(int id);
}
