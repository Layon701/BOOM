package itf221.gvi.boom.io;

import java.util.List;
import java.util.ArrayList;

import itf221.gvi.boom.data.Room;

public class RoomInterpreter {
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
