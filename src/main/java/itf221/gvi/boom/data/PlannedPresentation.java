package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Holds data for scheduled presentations.
 */
@AllArgsConstructor
@Data
public class PlannedPresentation {
    private char timeslot;
    private Room room;
    private OfferedPresentation offeredPresentation;
    private List<Student> attendees;
}
