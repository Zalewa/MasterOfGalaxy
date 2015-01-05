package masterofgalaxy.exceptions;

public class SavedGameException extends Exception {
    public SavedGameException(String message) {
        super(message);
    }

    public SavedGameException(String message, Exception cause) {
        super(message, cause);
    }
}
