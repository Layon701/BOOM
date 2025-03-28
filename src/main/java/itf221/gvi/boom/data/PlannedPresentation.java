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

    /**
     * Attempts to add a student to the list of attendees.
     *
     * @param student the student to be added
     * @return true if the student was successfully added, false if the max capacity has been reached
     */
    public boolean addStudent(Student student) {
        if (attendees.size() == offeredPresentation.getMaxCapacity()) {
            return false;
        }
        attendees.add(student);
        return true;
    }

    @Override
    public String toString() {
        return "PlannedPresentation{" +
                "timeslot=" + timeslot +
                ", room=" + room +
                ", offeredPresentation=" + offeredPresentation.getId() +
                ", attendees=" + attendees +
                '}';
    }
}
