package handlers;

import java.io.IOException;

public class SendHttpException extends RuntimeException {

    public SendHttpException(IOException e, String message) {
        super(e);
        System.out.println(message);
    }
}
