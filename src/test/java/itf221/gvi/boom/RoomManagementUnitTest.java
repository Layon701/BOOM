package itf221.gvi.boom;

import itf221.gvi.boom.data.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collections;
import java.util.List;
import java.util.Random;


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
        // Setup offered presentations
        List<OfferedPresentation> offeredPresentationList = new ArrayList<>(Arrays.asList(
                new OfferedPresentation(123, 0, 0, "", '0', ""),
                new OfferedPresentation(234, 0, 0, "", '0', ""),
                new OfferedPresentation(456, 0, 0, "", '0', ""),
                new OfferedPresentation(567, 0, 0, "", '0', ""),
                new OfferedPresentation(789, 0, 0, "", '0', ""),
                new OfferedPresentation(890, 0, 0, "", '0', ""),
                new OfferedPresentation(6969, 0, 0, "", '0', "")
        ));

        /*
         * For this test we simulate:
         * - Student1 with planned presentations that yield a score of 16
         *   (e.g. granted wishes: indices 0, 1, 3, 4 → 6+5+3+2 = 16).
         * - Student2 with planned presentations that yield a score of 17,
         *   achieved by granting one extra wish (e.g. indices 0, 1, 3, 4, and 5 → 6+5+3+2+1 = 17).
         */

        // Planned presentations for student1 (score 16)
        List<PlannedPresentation> plannedPresentationList1 = new ArrayList<>(Arrays.asList(
                new PlannedPresentation(offeredPresentationList.get(0)), // wish 0: 6 points
                new PlannedPresentation(offeredPresentationList.get(1)), // wish 1: 5 points
                new PlannedPresentation(offeredPresentationList.get(6)), // not granted (e.g. wish 2 missing)
                new PlannedPresentation(offeredPresentationList.get(3)), // wish 3: 3 points
                new PlannedPresentation(offeredPresentationList.get(4))  // wish 4: 2 points
        ));

        // Planned presentations for student2 (score 17, by adding one more granted wish)
        List<PlannedPresentation> plannedPresentationList2 = new ArrayList<>(Arrays.asList(
                new PlannedPresentation(offeredPresentationList.get(0)), // wish 0: 6 points
                new PlannedPresentation(offeredPresentationList.get(1)), // wish 1: 5 points
                new PlannedPresentation(offeredPresentationList.get(6)), // not granted (as above)
                new PlannedPresentation(offeredPresentationList.get(3)), // wish 3: 3 points
                new PlannedPresentation(offeredPresentationList.get(4)), // wish 4: 2 points
                new PlannedPresentation(offeredPresentationList.get(5))  // wish 5: 1 point (extra)
        ));

        // Create two students with different scores
        Student student1 = new Student(plannedPresentationList1, offeredPresentationList, null, null, null, 0);
        Student student2 = new Student(plannedPresentationList2, offeredPresentationList, null, null, null, 0);
        List<Student> studentList = Arrays.asList(student1, student2);

        BoomData boomData = new BoomData(null, null, studentList);
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();

        // Expected percentage: average score = (16 + 17)/2 = 16.5, then 16.5 * 5 = 82.5%
        double expectedValue = 82.5;
        double actualValue = roomManagementUnit.calculateCompletionScore(boomData);

        // Compare double values using a small delta for precision
        assertEquals(expectedValue, actualValue, 0.001);
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

    /**
     * Tests the pseudo random sorting of students list.
     * Random seed has to be same as in RoomManagementUnit!
     */
    @Test
    public void pseudoRandomizeStudentListTest() {
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        List<Student> students = createStudents(null, 200);
        roomManagementUnit.pseudoRandomizeStudentList(students);

        List<Student> students2 = createStudents(null, 200);
        Random random = new Random(12345);
        Collections.shuffle(students2, random);

        assertEquals(students, students2);
    }
}
