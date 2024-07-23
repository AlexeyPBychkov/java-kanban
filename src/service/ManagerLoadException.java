package service;

import java.io.IOException;

public class ManagerLoadException extends RuntimeException {

    public ManagerLoadException(IOException e, String message) {
        super(e);
        System.out.println(message);
    }
}
