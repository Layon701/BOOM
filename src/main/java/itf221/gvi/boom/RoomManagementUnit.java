package itf221.gvi.boom;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;

import java.util.List;

public class RoomManagementUnit {
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
     * Calculates the completion score of all students adn prints the percentage in the console.
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     */
    private void calculateCompletionScore(BoomData boomData) {
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
        System.out.println("Total score:" + totalScore / studentList.size() * 5 + "%");
    }

    /**
     * Checks if a wish from a student was granted and returns the boolean.
     * @param student the student where we want to check if the wish was granted.
     * @param i index of the wish.
     * @return boolean
     */
    private boolean isWishGranted(Student student, int i) {
        boolean isWished = false;
        for (PlannedPresentation plannedPresentation : student.getPlannedPresentations()) {
            if (plannedPresentation.getOfferedPresentation().equals(student.getOfferedPresentations().get(i))) {
                isWished = true;
                break;
            }
        }
        return isWished;
    }
}
