package itf221.gvi.boom.exceptions;

public class InterpretException extends RuntimeException {
    public InterpretException(String message) {
        super(message);
    }

    public InterpretException(String message, Exception e) {
        super(message, e);
    }
}
