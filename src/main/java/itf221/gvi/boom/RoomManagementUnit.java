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
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return a list of the PlannedPresentations after the
     */
    public List<PlannedPresentation> execute(BoomData boomData) {
        return null;
    }

    /**
     * Calculates the maximum possible presentations based on the number of time slots available for a presentation.
     * e.g. 'D' to 'E' = maximal presentation amount 2
     *
     * @param offeredPresentation the offered presentation.
     * @return the amount.
     */
    private int getMaxPossiblePresentations(OfferedPresentation offeredPresentation) {
        return this.lastPossibleTimeslot - offeredPresentation.getEarliestTime() + 1;
    }

    /**
     * Calculates the amount of presentations based on the student wishes.
     * Sets the calculated value as amountOfPresentations in the offeredPresentation.
     *
     * @param boomData the BoomData.
     */
    protected void setRequiredPresentationAmount(BoomData boomData) {
        Map<OfferedPresentation, Integer> studentWishesMap = new HashMap<>();

        for (Student student : boomData.getStudents()) {
            for (OfferedPresentation wishedPresentation : student.getWishedPresentations()) {
                studentWishesMap.merge(wishedPresentation, 1, Integer::sum);
            }
        }

        for (Map.Entry<OfferedPresentation, Integer> presentationWishAmountEntry : studentWishesMap.entrySet()) {
            int amountOfPresentations = (presentationWishAmountEntry.getValue() + 19) / 20;

            int maxPossiblePresentations = getMaxPossiblePresentations(presentationWishAmountEntry.getKey());
            if (amountOfPresentations > maxPossiblePresentations) {
                amountOfPresentations = maxPossiblePresentations;
            }

            presentationWishAmountEntry.getKey().setAmountOfPresentations(amountOfPresentations);
        }
    }

    /**
     * Calculates the completion score of all students adn prints the percentage in the console and returns the value.
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return int as the percentage of all scores
     */
    protected int calculateCompletionScore(BoomData boomData) {
        List<Student> studentList = boomData.getStudents();
        int studentScore = 0;
        int totalScore = 0;
        for (Student student : studentList) {
            for (int i = 0; i <= 5; i++) {
                if (isWishGranted(student, i)) {
                    studentScore += (6 - i);
                }
            }
            totalScore += studentScore;
            studentScore = 0;
        }
        int scorePercentage = totalScore / studentList.size() * 5;
        System.out.println("Total score:" + scorePercentage + "%");
        return scorePercentage;
    }

    /**
     * Checks if a wish from a student was granted and returns the boolean.
     *
     * @param student the student where we want to check if the wish was granted.
     * @param i       index of the wish.
     * @return boolean
     */
    private boolean isWishGranted(Student student, int i) {
        boolean isWished = false;
        for (PlannedPresentation plannedPresentation : student.getPlannedPresentations()) {
            if (plannedPresentation.getOfferedPresentation().equals(student.getWishedPresentations().get(i))) {
                isWished = true;
                break;
            }
        }
        return isWished;
    }
}
