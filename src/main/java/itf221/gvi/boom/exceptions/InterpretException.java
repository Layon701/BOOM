package itf221.gvi.boom.exceptions;

/**
 * Exception thrown to indicate an error occurred during interpretation or parsing of data.
 */
public class InterpretException extends RuntimeException {
    public InterpretException(String message) {
        super(message);
    }

    public InterpretException(String message, Exception e) {
        super(message, e);
    }
}
