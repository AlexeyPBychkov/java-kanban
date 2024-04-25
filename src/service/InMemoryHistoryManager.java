package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {
    ArrayList<T> historyList;
    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager(){
        historyList = new ArrayList<>();
    }

    @Override
    public List<T> getHistoryList() {
        return List.copyOf(historyList);
    }

    @Override
    public void add(T task) {
        if(task == null) {
            return;
        }
        if(historyList.size() == HISTORY_SIZE) {
            historyList.remove(0);
        }
        historyList.add(task);
    }
}
