package itf221.gvi.boom;

import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.PlannedPresentation;
import itf221.gvi.boom.data.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomManagementUnitTest {
    @Test
    public void calculateCompletionScoreTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //offeredPresentations
        List<OfferedPresentation> offeredPresentationList = new ArrayList<>();
        OfferedPresentation offeredPresentation1 = new OfferedPresentation(123, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation2 = new OfferedPresentation(234, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation3 = new OfferedPresentation(456, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation4 = new OfferedPresentation(567, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation5 = new OfferedPresentation(789, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation6 = new OfferedPresentation(890, 0 , 0, null, '0', null);
        OfferedPresentation offeredPresentation7 = new OfferedPresentation(6969, 0 , 0, null, '0', null);
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
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        Method calculateCompletionScore = RoomManagementUnit.class.getDeclaredMethod("calculateCompletionScore", BoomData.class);
        calculateCompletionScore.setAccessible(true);

        int expectedValue = 80;
        int actualValue = (int) calculateCompletionScore.invoke(roomManagementUnit, boomData);

        assertEquals(expectedValue, actualValue);
    }
}
