package itf221.gvi.boom.exceptions;

public class BoomDataCreationException extends RuntimeException {
    public BoomDataCreationException(String message) {
        super(message);
    }

    public BoomDataCreationException(String message, Exception e) {
        super(message, e);
    }
}
