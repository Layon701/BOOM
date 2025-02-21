package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds Room data.
 */
@AllArgsConstructor
@Getter
@Setter
public class Room {
    private String roomNumber;
    private int capacity;
}
