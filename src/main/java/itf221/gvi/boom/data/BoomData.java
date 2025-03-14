package itf221.gvi.boom.data;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Holds all relevant BOOM data.
 */
@AllArgsConstructor
@Data
public class BoomData {
    private List<Room> rooms;
    private List<Company> companies;
    private List<Student> students;
}
