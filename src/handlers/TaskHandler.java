package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String[] str = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (str.length == 2 && str[1].equals("tasks")) {
                    sendText(exchange, gson.toJson(tm.getTasks()));
                }
                if (str.length > 2) {
                    Task task = tm.getTaskById(Integer.parseInt(str[2]));
                    if (task == null) {
                        sendNotFound(exchange);
                    } else {
                        sendText(exchange, gson.toJson(task));
                    }
                }
                break;
            case "POST":
                if (str.length == 2 && str[1].equals("tasks")) {
                    Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8), Task.class);
                    if (task != null) {
                        if (tm.createTask(task)) {
                            sendPostSuccess(exchange);
                        } else {
                            sendHasInteractions(exchange);
                        }
                    }
                }
                if (str.length > 2) {
                    Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8), Task.class);
                    if (task != null) {
                        task.setId(Integer.parseInt(str[2]));
                        tm.updateTask(task);
                        sendPostSuccess(exchange);
                    }
                }
                break;
            case "DELETE":
                if (str.length > 2) {
                    Task task = tm.getTaskById(Integer.parseInt(str[2]));
                    if (task == null) {
                        sendNotFound(exchange);
                    } else {
                        tm.deleteTaskById(Integer.parseInt(str[2]));
                        sendText(exchange, "Task deleted");
                    }
                }
            default:
                sendNotFound(exchange);
                break;
        }
    }
}
