package handlers;

import service.TaskManager;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager tm) {
        this.tm = tm;
    }
}
