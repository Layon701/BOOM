package itf221.gvi.boom;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Tests for the RoomManagementUnit class
 */
public class RoomManagementUnitTest {


    /**
     * Tests the setRequiredPresentationAmount() method
     * case a: regular case - round up student wishes / 20
     * case b: tests if earliest timeslot bottleneck is applied correctly (e.g. 'D' to 'E' = maximal presentation amount 2)
     */
    @Test
    public void testAmountOfPresentations() {
        Company tmpCompanyA = new Company("CompanyA", List.of(new OfferedPresentation(1, 0, 20, "specialtyA", 'A', "CompanyA")));
        Company tmpCompanyB = new Company("CompanyB", List.of(new OfferedPresentation(2, 0, 20, "specialtyB", 'D', "CompanyB")));

        List<OfferedPresentation> wishes = new ArrayList<>();
        wishes.add(tmpCompanyA.getOfferedPresentations().getFirst());
        wishes.add(tmpCompanyB.getOfferedPresentations().getFirst());

        List<Student> students = createStudents(wishes, 42);

        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();

        BoomData boomData = new BoomData(null, null, students);
        roomManagementUnit.setRequiredPresentationAmount(boomData);

        assertEquals(3, tmpCompanyA.getOfferedPresentations().getFirst().getAmountOfPresentations());
        assertEquals(2, tmpCompanyB.getOfferedPresentations().getFirst().getAmountOfPresentations());
    }

    private List<Student> createStudents(List<OfferedPresentation> wishes, int numberOfStudents) {
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= numberOfStudents; i++) {
            students.add(new Student(wishes, "surname" + i, "name" + i, "Class" + i, i));
        }
        return students;
    }

    @Test
    public void calculateCompletionScoreTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //offeredPresentations
        List<OfferedPresentation> offeredPresentationList = new ArrayList<>();
        OfferedPresentation offeredPresentation1 = new OfferedPresentation(123, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation2 = new OfferedPresentation(234, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation3 = new OfferedPresentation(456, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation4 = new OfferedPresentation(567, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation5 = new OfferedPresentation(789, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation6 = new OfferedPresentation(890, 0, 0, null, '0', null);
        OfferedPresentation offeredPresentation7 = new OfferedPresentation(6969, 0, 0, null, '0', null);
        offeredPresentationList.add(offeredPresentation1);
        offeredPresentationList.add(offeredPresentation2);
        offeredPresentationList.add(offeredPresentation3);
        offeredPresentationList.add(offeredPresentation4);
        offeredPresentationList.add(offeredPresentation5);
        offeredPresentationList.add(offeredPresentation6);
        //plannedPresentations
        List<PlannedPresentation> plannedPresentationList = new ArrayList<>();
        PlannedPresentation plannedPresentation1 = new PlannedPresentation('0', null, offeredPresentation1, null);
        PlannedPresentation plannedPresentation2 = new PlannedPresentation('0', null, offeredPresentation2, null);
        PlannedPresentation plannedPresentation3 = new PlannedPresentation('0', null, offeredPresentation7, null);
        PlannedPresentation plannedPresentation4 = new PlannedPresentation('0', null, offeredPresentation4, null);
        PlannedPresentation plannedPresentation5 = new PlannedPresentation('0', null, offeredPresentation5, null);
        plannedPresentationList.add(plannedPresentation1);
        plannedPresentationList.add(plannedPresentation2);
        plannedPresentationList.add(plannedPresentation3);
        plannedPresentationList.add(plannedPresentation4);
        plannedPresentationList.add(plannedPresentation5);
        //students
        List<Student> studentList = new ArrayList<>();
        //completionscore for student1: 6+5+3+2=16 * 5 = 80%
        Student student1 = new Student(plannedPresentationList, offeredPresentationList, null, null, null, 0);
        studentList.add(student1);
        BoomData boomData = new BoomData(null, null, studentList);
        Method calculateCompletionScore = RoomManagementUnit.class.getDeclaredMethod("calculateCompletionScore", BoomData.class);
        calculateCompletionScore.setAccessible(true);
        int expectedValue = 80;
        int actualValue = (int) calculateCompletionScore.invoke(roomManagementUnit, boomData);
        assertEquals(expectedValue, actualValue);
    }
}
