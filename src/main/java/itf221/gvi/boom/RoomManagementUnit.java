package itf221.gvi.boom;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class RoomManagementUnit {

    private final char lastPossibleTimeslot = 'E';

    /**
     * <b>Deterministic</b> algorithm for distributing the offered presentations among the students considering their wishes.
     * <p>
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return a list of the PlannedPresentations after the
     */
    public List<PlannedPresentation> execute(BoomData boomData) {
        return null;
    }

    /**
     * Calculates the maximum possible presentations based on the number of time slots available for a presentation.
     * @param offeredPresentation the offered presentation.
     * @return the amount.
     */
    private int getMaxPossiblePresentations(OfferedPresentation offeredPresentation) {
        return this.lastPossibleTimeslot - offeredPresentation.getEarliestTime() + 1;
    }

    /**
     * Calculates the amount of presentations based on the student wishes.
     * This defines how many plannedPresentations should be instantiated.
     * @param boomData the BoomData.
     */
    private void getRequiredPresentationAmount(BoomData boomData) {
        Map<OfferedPresentation, Integer> studentWishesMap = new HashMap<>();

        for (Student student : boomData.getStudents()) {
            for (OfferedPresentation wishedPresentation : student.getWishedPresentations()) {
                Integer currentStudentAmount = studentWishesMap.get(wishedPresentation);
                currentStudentAmount += 1;
                studentWishesMap.put(wishedPresentation, currentStudentAmount);
            }
        }

        for (Map.Entry<OfferedPresentation, Integer> presentationWishAmountEntry : studentWishesMap.entrySet()) {
            int amountOfPresentations = presentationWishAmountEntry.getValue() / 20;

            if (amountOfPresentations > getMaxPossiblePresentations(presentationWishAmountEntry.getKey())) {
                amountOfPresentations = getMaxPossiblePresentations(presentationWishAmountEntry.getKey());
            }

            presentationWishAmountEntry.getKey().setAmountOfPresentations(amountOfPresentations);
        }
    }


}
