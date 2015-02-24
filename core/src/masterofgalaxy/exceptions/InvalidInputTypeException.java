package masterofgalaxy.exceptions;

public class InvalidInputTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidInputTypeException(String message) {
        super(message);
    }
}
