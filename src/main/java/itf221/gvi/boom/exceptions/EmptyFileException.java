package itf221.gvi.boom.exceptions;

/**
 * Exception thrown to indicate that a file is unexpectedly empty.
 */
public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String message) {
        super(message);
    }
}
