package itf221.gvi.boom.data;

import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OfferedPresentationTest {

    @Test
    public void getPlannedPresentationWithLeastAmountOfAttendeesTest() {
        OfferedPresentation o1 = new OfferedPresentation(1, 5, 20,
                "Polizeikommisar*in", 'A', "Polizei");

        assertNull(o1.getPlannedPresentationWithLeastAmountOfAttendees());

        OfferedPresentation o2 = new OfferedPresentation(1, 5, 20,
                "Polizeikommisar*in", 'A', "Polizei");

        List<PlannedPresentation> plannedPresentations = new ArrayList<>(3);
        plannedPresentations.add(new PlannedPresentation(o2));
        plannedPresentations.add(new PlannedPresentation(o2));
        plannedPresentations.add(new PlannedPresentation(o2));
        plannedPresentations.get(1).addStudent(new Student(null, "", "", "", 0));


        o2.setPlannedPresentations(plannedPresentations);

        assertEquals(plannedPresentations.get(1), o2.getPlannedPresentationWithLeastAmountOfAttendees());

        plannedPresentations.get(1).addStudent(new Student(null, "", "", "", 0));
        plannedPresentations.get(2).addStudent(new Student(null, "", "", "", 0));
    }

    /**
     * Tests the getTitle Method.
     */
    @Test
    public void getTitleTest() {
        OfferedPresentation offeredPresentation =
                new OfferedPresentation(1, 5, 20,
                        "Polizeikommisar*in", 'A', "Polizei");

        String expectedTitle = "Polizei: Polizeikommisar*in";
        assertEquals(expectedTitle, offeredPresentation.getTitle());
    }
}
