package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Holds Room data.
 */
@AllArgsConstructor
@Data
public class Room {
    private String roomNumber;
    private int capacity;
}
