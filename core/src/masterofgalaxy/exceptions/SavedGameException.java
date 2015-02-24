package masterofgalaxy.exceptions;

public class SavedGameException extends Exception {
    private static final long serialVersionUID = 1L;

    public SavedGameException(String message) {
        super(message);
    }

    public SavedGameException(String message, Exception cause) {
        super(message, cause);
    }
}
