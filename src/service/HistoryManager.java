package service;

import model.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {
    List<T> getHistoryList();

    void add(T task);
}
