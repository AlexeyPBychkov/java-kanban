package model;

public class Subtask extends Task{
    private Epic parentEpic;

    public Subtask(String title, String description, TaskStatus status, Epic parentEpic) {
        super(title, description, status);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }
}
