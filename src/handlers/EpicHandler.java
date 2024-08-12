package handlers;

import service.TaskManager;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager tm) {
        this.tm = tm;
    }
}
