package converter;

import model.Task;

public class TaskConverter {

    public String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus()
                + "," + task.getDescription() + "," + task.getEpicId() + "\n";
    }

//    private TaskType getTaskType(Task task) {
//        if(task instanceof Task) {
//            return TaskType.TASK;
//        } else if(task instanceof Epic) {
//            return TaskType.EPIC;
//        } else if (task instanceof Subtask) {
//            return TaskType.SUBTASK;
//        }
//        return null;
//    }
}
