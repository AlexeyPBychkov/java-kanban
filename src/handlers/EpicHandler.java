package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import adapters.SubtasksListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Subtask.class, new SubtasksListAdapter())
                .create();
        String[] str = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (str.length == 2 && str[1].equals("epics")) {
                    sendText(exchange, gson.toJson(tm.getEpics()));
                }
                if (str.length == 3) {
                    Epic epic = tm.getEpicById(Integer.parseInt(str[2]));
                    if (epic == null) {
                        sendNotFound(exchange);
                    } else {
                        sendText(exchange, gson.toJson(epic));
                    }
                }
                if (str.length == 4 && str[3].equals("subtasks")) {
                    Epic epic = tm.getEpicById(Integer.parseInt(str[2]));
                    if (epic == null) {
                        sendNotFound(exchange);
                    } else {
                        ArrayList<Subtask> subtasks = tm.getSubtasksByEpicId(Integer.parseInt(str[2]));
                        sendText(exchange, gson.toJson(subtasks));
                    }
                }
                break;
            case "POST":
                if (str.length == 2 && str[1].equals("epics")) {
                    Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8), Epic.class);
                    if (tm.createEpic(epic)) {
                        sendPostSuccess(exchange);
                    } else {
                        sendHasInteractions(exchange);
                    }
                }
                break;
            case "DELETE":
                if (str.length > 2) {
                    Epic epic = tm.getEpicById(Integer.parseInt(str[2]));
                    if (epic == null) {
                        sendNotFound(exchange);
                    } else {
                        tm.deleteEpicById(Integer.parseInt(str[2]));
                        sendText(exchange, "Epic deleted");
                    }
                }
            default:
                sendNotFound(exchange);
                break;
        }
    }
}
