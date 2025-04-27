package itf221.gvi.boom.exceptions;

/**
 * Exception thrown to indicate an error occurred during the creation of Boom data.
 */
public class BoomDataCreationException extends RuntimeException {
    public BoomDataCreationException(String message) {
        super(message);
    }

    public BoomDataCreationException(String message, Exception e) {
        super(message, e);
    }
}
