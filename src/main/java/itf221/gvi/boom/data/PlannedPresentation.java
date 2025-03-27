package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data for scheduled presentations.
 */
@RequiredArgsConstructor
@Data
public class PlannedPresentation {
    private char timeslot;
    private Room room;
    @NonNull
    private OfferedPresentation offeredPresentation;
    private List<Student> attendees = new ArrayList<>();

    public int addStudent(Student student) {
        if (attendees.size() == offeredPresentation.getMaxCapacity()) {
            return -1;
        }
        attendees.add(student);
        return 1;
    }
}
