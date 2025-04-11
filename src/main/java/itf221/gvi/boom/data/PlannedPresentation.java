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
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class PlannedPresentation {
    private char timeslot;
    private Room room;
    @NonNull
    private OfferedPresentation offeredPresentation;
    private List<Student> attendees = new ArrayList<>();

    /**
     * Attempts to add a student to the list of attendees. If added, the presentation reference is added to student.
     *
     * @param student the student to be added.
     * @return true if the student was successfully added, false if the max capacity has been reached.
     */
    public boolean addStudent(Student student) {
        if (attendees.size() == offeredPresentation.getMaxCapacity()) {
            return false;
        }
        attendees.add(student);
        student.getPlannedPresentations().add(this);
        return true;
    }

    /**
     * Removes student from the list of attendees and removes reference in student.
     *
     * @param student the student to remove.
     */
    public void removeStudent(Student student) {
        if (attendees.remove(student)) {
            student.getPlannedPresentations().remove(this);
        }
    }

    @Override
    public String toString() {
        return "PlannedPresentation{" +
                "timeslot=" + timeslot +
                ", room=" + room +
                ", offeredPresentation=" + offeredPresentation.getId() +
                ", attendees=" + attendees.size() +
                '}';
    }
}
