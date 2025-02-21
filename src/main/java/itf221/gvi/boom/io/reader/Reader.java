package itf221.gvi.boom.io.reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for readers with a readFile method
 */
public interface Reader {
    List<List<String>> readFile(Path path) throws IOException;
}
