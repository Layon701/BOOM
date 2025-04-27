package itf221.gvi.boom.io;

import itf221.gvi.boom.data.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomInterpreterTest {

    /**
     * Tests the interpret method from the RoomInterpreter class.
     */
    @Test
    void interpretTest() {

        Room tmpRoom1 = new Room("1", 20);
        Room tmpRoom2 = new Room("20", 20);
        Room tmpRoom3 = new Room("Aula", 40);

        List<Room> expectedData = Arrays.asList(tmpRoom1, tmpRoom2, tmpRoom3);

        // initializer list by scheme: roomNumber, capacity
        ArrayList<String> tmpRow1 = new ArrayList<String>(Arrays.asList("1", "20"));
        ArrayList<String> tmpRow2 = new ArrayList<String>(Arrays.asList("20", "20"));
        ArrayList<String> tmpRow3 = new ArrayList<String>(Arrays.asList("Aula", "40"));

        List<List<String>> testParameters = new ArrayList<>(Arrays.asList(tmpRow1, tmpRow2, tmpRow3));

        List<Room> testData = RoomInterpreter.interpret(testParameters);
        assertEquals(expectedData, testData);

    }
}
