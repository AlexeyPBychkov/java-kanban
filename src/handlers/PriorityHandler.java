package handlers;

import adapters.DurationAdapter;
import adapters.EpicAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class PriorityHandler extends BaseHttpHandler {

    public PriorityHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter(tm))
                .create();
        String[] str = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (str.length == 2 && str[1].equals("prioritized")) {
                    sendText(exchange, gson.toJson(tm.getPrioritizedTasks()));
                }
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }
}
