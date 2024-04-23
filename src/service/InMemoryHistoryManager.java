package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {
    ArrayList<T> historyList;

    public InMemoryHistoryManager(){
        historyList = new ArrayList<>();
    }

    @Override
    public ArrayList<T> getHistoryList() {
        return historyList;
    }

    @Override
    public void add(T task) {
        if(historyList.size() == 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }
}
