package itf221.gvi.boom.io;

import java.util.List;
import java.util.ArrayList;

import itf221.gvi.boom.data.Room;

/**
 * Interpreter for the room Excel-sheet
 */
public class RoomInterpreter {
	/**
	 * Creates a list of Room objects
	 * @param data List<List<String>> (from the Xlsx-Reader)
	 * @return List<Room>
	 */
	public static List<Room> interpret(List<List<String>> data)
	{
		ArrayList<Room> rooms = new ArrayList<Room>();
		for(List<String> ls : data)
		{
			if(ls.size() != 2)
			{
				continue;
			}

			rooms.add(new Room(ls.get(0), Integer.parseInt(ls.get(1))));
		}
		return rooms;
	}
}
