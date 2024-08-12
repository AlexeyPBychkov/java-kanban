import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import adapters.SubtasksListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServer {

    private static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Задача 11", "Описание задачи 11", TaskStatus.NEW);

        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(180));
        tm.createTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2");
        Subtask subtask1 = new Subtask("Subtask 1", "Description subtask 1", TaskStatus.NEW, epic1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description subtask 2", TaskStatus.NEW, epic1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description subtask 3", TaskStatus.NEW, epic1);
        subtask1.setStartTime(LocalDateTime.now().minusDays(1));
        subtask1.setDuration(Duration.ofMinutes(180));
        subtask2.setStartTime(LocalDateTime.now().minusDays(2));
        subtask2.setDuration(Duration.ofMinutes(180));
        subtask3.setStartTime(LocalDateTime.now().minusDays(3));
        subtask3.setDuration(Duration.ofMinutes(180));

        tm.createEpic(epic1);
        tm.createEpic(epic2);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        tm.createSubtask(subtask3);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(tm));
        httpServer.createContext("/subtasks", new SubtaskHandler(tm));
        httpServer.createContext("/epics", new EpicHandler(tm));
        httpServer.createContext("/history", new HistoryHandler(tm));
        httpServer.createContext("/prioritized", new PriorityHandler(tm));
        httpServer.start();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Subtask.class, new SubtasksListAdapter())
                .create();

        ArrayList<Epic> epics = tm.getEpics();
        String obj = gson.toJson(tm.getEpics());

        System.out.println("HTTP-сервер запущен");
    }

}

