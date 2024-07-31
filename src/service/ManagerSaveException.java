package service;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(IOException e) {
        super(e);
    }

    public ManagerSaveException(IOException e, String message) {
        super(e);
        System.out.println(message);
    }
}
