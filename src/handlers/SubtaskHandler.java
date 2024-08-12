package handlers;

import service.TaskManager;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager tm) {
        this.tm = tm;
    }
}
