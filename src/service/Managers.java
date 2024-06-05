package service;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        //return new InMemoryTaskManager(getDefaultHistory());
        return new FileBackedTaskManager(new InMemoryHistoryManager());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(new InMemoryHistoryManager(), file);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
