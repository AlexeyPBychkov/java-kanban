package model;

public class Subtask extends Task {
    private Epic parentEpic;

    public Subtask(String title, String description, TaskStatus status, Epic parentEpic) {
        super(title, description, status);
        this.parentEpic = parentEpic;
//        this.parentEpic.addSubtask(this);
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicID=" + parentEpic.getId() +
                '}';
    }

    @Override
    public String getEpicId() {
        return Integer.toString(parentEpic.getId());
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
