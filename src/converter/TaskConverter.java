package converter;

import model.Task;

public class TaskConverter {

    public String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus()
                + "," + task.getDescription() + "," + task.getEpicId() + "\n";
    }

}
