package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {

    protected TaskManager tm;

    protected void sendText(HttpExchange h, String text) {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try {
            h.sendResponseHeaders(200, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        } catch (IOException e) {
            throw new SendHttpException(e, "Не могу отправить ответ");
        }

    }

    protected void sendNotFound(HttpExchange h) {
        try {
            h.sendResponseHeaders(404, 0);
            h.getResponseBody().write("Not found".getBytes(StandardCharsets.UTF_8));
            h.close();
        } catch (IOException e) {
            throw new SendHttpException(e, "Не могу отправить ответ");
        }

    }

    protected void sendHasInteractions(HttpExchange h) {
        try {
            h.sendResponseHeaders(406, 0);
            h.getResponseBody().write("Has Interaction".getBytes(StandardCharsets.UTF_8));
            h.close();
        } catch (IOException e) {
            throw new SendHttpException(e, "Не могу отправить ответ");
        }
    }

    protected void sendPostSuccess(HttpExchange h) {
        try {
            h.sendResponseHeaders(201, 0);
            h.getResponseBody().write("Success".getBytes(StandardCharsets.UTF_8));
            h.close();
        } catch (IOException e) {
            throw new SendHttpException(e, "Не могу отправить ответ");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
