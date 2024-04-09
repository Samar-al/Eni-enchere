package fr.eni.tp.enienchere.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<String> keys = new ArrayList<>();

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void add(String message) {
        keys.add(message);
    }
}
