package service;

import converter.TaskConverter;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static String HEADERS_FILE_STRING = "id,type,name,status,description,epic,startTime,endTime,duration";
    private final File file = new File("task.csv");

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        loadFromFile(file);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(HEADERS_FILE_STRING + "\n");
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private void loadFromFile(File file) {
        int maxId = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, UTF_8))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                Task task = fromString(line);
                int id = task.getId();
                if (maxId < id) {
                    maxId = id;
                }
                if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                } else if (task.getType() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    Subtask subtask = (Subtask) task;
                    subtasks.put(id, subtask);
                    subtask.getParentEpic().addSubtask(subtask);
                }
            }
            seqId = maxId;
        } catch (IOException e) {
            throw new ManagerLoadException(e, "Не могу загрузить из файла");
        }
    }

    private String toString(Task value) {
        return new TaskConverter().toString(value);
    }

    private Task fromString(String value) {
        String[] taskArray = value.split(",");
        Task task;
        switch (TaskType.valueOf(taskArray[1])) {
            case TaskType.TASK:
                task = new Task(taskArray[2], taskArray[4], (getStatus(taskArray[3])));
                task.setId(Integer.parseInt(taskArray[0]));
                task.setStartTime(LocalDateTime.parse(taskArray[6]));
//                task.setDuration(Duration.ofMinutes(Integer.parseInt(taskArray[8])));
                task.setDuration(Duration.parse(taskArray[8]));
                prioritizedTasks.add(task);
                break;
            case TaskType.EPIC:
                task = new Epic(taskArray[2], taskArray[4]);
                task.setId(Integer.parseInt(taskArray[0]));
                task.setStatus(getStatus(taskArray[3]));
                if (!taskArray[6].equals("null")) {
                    task.setStartTime(LocalDateTime.parse(taskArray[6]));
                    ((Epic) task).setEndTime(LocalDateTime.parse(taskArray[7]));
//                task.setDuration(Duration.ofMinutes(Integer.parseInt(taskArray[8])));
                    task.setDuration(Duration.parse(taskArray[8]));
                    prioritizedTasks.add(task);
                }
                break;
            case TaskType.SUBTASK:
                task = new Subtask(taskArray[2], taskArray[4], (getStatus(taskArray[3])),
                        getEpicById(Integer.parseInt(taskArray[5])));
                task.setId(Integer.parseInt(taskArray[0]));
                task.setStartTime(LocalDateTime.parse(taskArray[6]));
//                task.setDuration(Duration.ofMinutes(Integer.parseInt(taskArray[8])));
                task.setDuration(Duration.parse(taskArray[8]));
                prioritizedTasks.add(task);
                break;
            default:
                task = null;
        }
        return task;
    }

    private TaskStatus getStatus(String value) {
        return TaskStatus.valueOf(value);
    }


}
