package itf221.gvi.boom.data;

import itf221.gvi.boom.io.reader.XlsxReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OfferedPresentationTest {
    /**
     * Tests the getTitle Method.
     */
    @Test
    public void getTitleTest() throws IOException {
        OfferedPresentation offeredPresentation =
                new OfferedPresentation(1, 5, 20,
                        "Polizeikommisar*in", 'A', "Polizei");

        String expectedTitle = "Polizei: Polizeikommisar*in";
        assertEquals(expectedTitle, offeredPresentation.getTitle());
    }
}
