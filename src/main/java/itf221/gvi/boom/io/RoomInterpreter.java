package itf221.gvi.boom.io;

import java.util.List;
import java.util.ArrayList;

import itf221.gvi.boom.data.Room;
import itf221.gvi.boom.data.Student;
import itf221.gvi.boom.exceptions.InterpretException;

/**
 * Interpreter for the room Excel-sheet
 */
public class RoomInterpreter {
    /**
     * Creates a list of Room objects
     *
     * @param data List<List<String>> (from the Xlsx-Reader)
     * @return List<Room>
     */
    public static List<Room> interpret(List<List<String>> data) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        for (List<String> row : data) {
            if (row.size() != 2) {
                continue;
            }

            try {
                String name = row.get(0);
                int capacity = Integer.parseInt(row.get(1));

                rooms.add(new Room(name, capacity));
            } catch (IndexOutOfBoundsException e) {
                throw new InterpretException("Error while trying to get indexes for Room object", e);
            }
        }
        System.out.println("Success creating rooms from room data");
        return rooms;
    }
}
