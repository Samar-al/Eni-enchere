package fr.eni.tp.enienchere.exception;

public class UserPasswordException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public UserPasswordException() {
        super();
    }

    public UserPasswordException(String message) {
        super(message);
    }

    public UserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
