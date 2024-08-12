import com.sun.net.httpserver.HttpServer;
import handlers.*;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Задача 11", "Описание задачи 11", TaskStatus.NEW);

        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(180));
        tm.createTask(task1);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(tm));
        httpServer.createContext("/subtasks", new SubtaskHandler(tm));
        httpServer.createContext("/epics", new EpicHandler(tm));
        httpServer.createContext("/history", new HistoryHandler(tm));
        httpServer.createContext("/prioritized", new PriorityHandler(tm));
        httpServer.start();

        System.out.println("HTTP-сервер запущен");
    }

}

