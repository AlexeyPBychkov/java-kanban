package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager tm) {
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
                if (str.length == 2 && str[1].equals("subtasks")) {
                    sendText(exchange, gson.toJson(tm.getSubtasks()));
                }
                if (str.length > 2) {
                    Subtask subtask = tm.getSubtaskById(Integer.parseInt(str[2]));
                    if (subtask == null) {
                        sendNotFound(exchange);
                    } else {
                        sendText(exchange, gson.toJson(subtask));
                    }
                }
                break;
            case "POST":
                if (str.length == 2 && str[1].equals("subtasks")) {
                    Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                            , StandardCharsets.UTF_8), Subtask.class);
                    if (tm.createSubtask(subtask)) {
                        sendPostSuccess(exchange);
                    } else {
                        sendHasInteractions(exchange);
                    }
                }
                if (str.length > 2) {
                    Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                            , StandardCharsets.UTF_8), Subtask.class);
                    tm.updateSubtask(subtask);
                    sendPostSuccess(exchange);
                }
                break;
            case "DELETE":
                if (str.length > 2) {
                    Subtask subtask = tm.getSubtaskById(Integer.parseInt(str[2]));
                    if (subtask == null) {
                        sendNotFound(exchange);
                    } else {
                        tm.deleteSubtaskById(Integer.parseInt(str[2]));
                        sendText(exchange, "Subtask deleted");
                    }
                }
            default:
                sendNotFound(exchange);
                break;
        }
    }
}
