package itf221.gvi.boom.io.reader;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTest {
    /**
     * Tests the readFile method from the XlsxReader class
     * @throws IOException when test data is missing
     */
    @Test
    public void readFileTest() throws IOException {
        Path testPath = new File("src/test/resources/TestExcelData.xlsx").toPath();
        XlsxReader xlsxReader = new XlsxReader();
        List<List<String>> testData = xlsxReader.readFile(testPath);
        List<List<String>> expectedData = new ArrayList<>();

        List<String> temp1 = new ArrayList<>();
        temp1.add("String");
        temp1.add("Number");
        temp1.add("More Numbers");
        expectedData.add(temp1);

        List<String> temp2 = new ArrayList<>();
        temp2.add("test");
        temp2.add("123.0");
        temp2.add("456.0");
        expectedData.add(temp2);

        List<String> temp3 = new ArrayList<>();
        temp3.add("test2");
        temp3.add("234");
        temp3.add("567.0");
        expectedData.add(temp3);

        List<String> temp4 = new ArrayList<>();
        temp4.add("empty next cell");
        temp4.add("");
        temp4.add("678.0");
        expectedData.add(temp4);

        assertEquals(expectedData, testData);
    }
}
