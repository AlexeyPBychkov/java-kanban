package handlers;

import service.TaskManager;

public class PriorityHandler extends BaseHttpHandler {

    public PriorityHandler(TaskManager tm) {
        this.tm = tm;
    }
}
