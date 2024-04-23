package service;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager<T extends Task> {
    ArrayList<T> getHistoryList();

    void add(T task);
}
