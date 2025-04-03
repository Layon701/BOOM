package itf221.gvi.boom;

import itf221.gvi.boom.data.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public void setRequiredPresentationAmountTest() {
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
    public void calculateCompletionScoreTest() {
        //offeredPresentations
        List<OfferedPresentation> offeredPresentationList = new ArrayList<>();
        OfferedPresentation offeredPresentation1 = new OfferedPresentation(123, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation2 = new OfferedPresentation(234, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation3 = new OfferedPresentation(456, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation4 = new OfferedPresentation(567, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation5 = new OfferedPresentation(789, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation6 = new OfferedPresentation(890, 0, 0, "", '0', "");
        OfferedPresentation offeredPresentation7 = new OfferedPresentation(6969, 0, 0, "", '0', "");
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

        int expectedValue = 80;
        int actualValue = roomManagementUnit.calculateCompletionScore(boomData);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testPolizeiAlwaysGetsAula() {
        Room aula = new Room("Aula", 100);
        Room room2 = new Room("R2", 150);
        List<Room> rooms = Arrays.asList(aula, room2);

        OfferedPresentation op = new OfferedPresentation(1, 50, 200, "Anzeigenhauptmeister", 'A', "Polizei");
        op.setAmountOfPresentations(1);

        Company polizei = new Company("Polizei", List.of(op));
        List<Company> companies = List.of(polizei);

        List<Student> students = new ArrayList<>();

        BoomData boomData = new BoomData(rooms, companies, students);
        RoomManagementUnit rmu = new RoomManagementUnit();

        rmu.setTimeslotAndRoom(boomData);

        // verify that plannedPresentations are initialized and filled
        assertNotNull(op.getPlannedPresentations(), "Planned presentations should not be null");
        assertEquals(1, op.getPlannedPresentations().size(), "exactly one plannedPresentation should be here");
        PlannedPresentation pp = op.getPlannedPresentations().getFirst();
        assertEquals("Aula", pp.getRoom().getRoomNumber(), "Company 'Polizei' should receive room 'Aula'");
    }

    @Test
    public void testContiguousTimeslotsAndNoOverbooking() {
        Room room1 = new Room("R1", 150);
        List<Room> rooms = List.of(room1);

        OfferedPresentation op = new OfferedPresentation(2, 50, 200, "Technology", 'A', "TechCompany");
        op.setAmountOfPresentations(3);

        // simulate that one presentation already exists
        List<PlannedPresentation> preScheduled = new ArrayList<>();
        preScheduled.add(new PlannedPresentation('A', room1, op, new ArrayList<>()));
        op.setPlannedPresentations(preScheduled);

        Company company = new Company("TechCompany", List.of(op));
        List<Company> companies = List.of(company);

        List<Student> students = new ArrayList<>();
        BoomData boomData = new BoomData(rooms, companies, students);

        RoomManagementUnit rmu = new RoomManagementUnit();
        rmu.setTimeslotAndRoom(boomData);

        assertEquals(3, op.getPlannedPresentations().size(), "The OfferedPresentation should have 3 planned presentations");

        // check that timeslots are consecutive
        List<Character> timeslots = new ArrayList<>();
        for (PlannedPresentation pp : op.getPlannedPresentations()) {
            timeslots.add(pp.getTimeslot());
        }
        Collections.sort(timeslots);
        System.out.println(timeslots);
        assertEquals('A', timeslots.get(0));
        assertEquals('B', timeslots.get(1));
        assertEquals('C', timeslots.get(2));
    }
}
