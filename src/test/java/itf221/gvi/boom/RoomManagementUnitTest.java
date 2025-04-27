package itf221.gvi.boom;

import itf221.gvi.boom.data.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the RoomManagementUnit class
 */
public class RoomManagementUnitTest {

    @Test
    public void distributeStudentsTest() {

    }


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

        // Expected percentage: average score = (16 + 17)/2 = 16.5, then 16.5 * 5 = 82.5% -> but since we don't use double anymore, it's going to be 82%
        double expectedValue = 82.0;
        double actualValue = roomManagementUnit.calculateCompletionScore(boomData);

        // Compare double values using a small delta for precision
        assertEquals(expectedValue, actualValue, 0.001);
    }

    /**
     * creates dummy Data
     * @return dummy BoomData object
     */
    public BoomData createTestBoomData() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Aula", 50));    // Room reserved for "Polizei"
        rooms.add(new Room("Room1", 20));
        rooms.add(new Room("Room2", 20));
        rooms.add(new Room("Room3", 15));
        rooms.add(new Room("Room4", 20));
        rooms.add(new Room("Room5", 20));
        rooms.add(new Room("Room6", 20));

        // Create OfferedPresentations with planned presentation amounts.
        OfferedPresentation opPolizei = new OfferedPresentation(1, 5, 25, "Security", 'A', "Polizei");
        opPolizei.setAmountOfPresentations(2);

        OfferedPresentation opTech = new OfferedPresentation(2, 5, 20, "IT", 'B', "TechCorp");
        opTech.setAmountOfPresentations(2);

        OfferedPresentation opHealth = new OfferedPresentation(3, 5, 15, "Healthcare", 'A', "HealthInc");
        opHealth.setAmountOfPresentations(3);

        OfferedPresentation opWoop1 = new OfferedPresentation(4, 5, 20, "opWoop1", 'A', "opWoop1");
        opWoop1.setAmountOfPresentations(2);

        OfferedPresentation opWoop2 = new OfferedPresentation(5, 5, 20, "opWoop2", 'A', "opWoop2");
        opWoop2.setAmountOfPresentations(2);

        OfferedPresentation opWoop3 = new OfferedPresentation(6, 5, 20, "opWoop3", 'A', "opWoop3");
        opWoop3.setAmountOfPresentations(3);

        // Create Companies; each company gets its respective offered presentation.
        Company polizeiCompany = new Company("Polizei", List.of(opPolizei));
        Company techCorpCompany = new Company("TechCorp", List.of(opTech));
        Company healthIncCompany = new Company("HealthInc", List.of(opHealth));
        Company woop1Company = new Company("woop1", List.of(opWoop1));
        Company woop2Company = new Company("woop2", List.of(opWoop2));
        Company woop3Company = new Company("woop3", List.of(opWoop3));

        List<Company> companies = new ArrayList<>();
        companies.add(polizeiCompany);
        companies.add(techCorpCompany);
        companies.add(healthIncCompany);
        companies.add(woop1Company);
        companies.add(woop2Company);
        companies.add(woop3Company);

        // Create multiple Students
        int numStudents = 10;
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= numStudents; i++) {

            // Prepare the wished presentations: the list length is 6.
            List<OfferedPresentation> wishes = new ArrayList<>();
            wishes.add(opPolizei);
            wishes.add(opTech);
            wishes.add(opHealth);
            wishes.add(opWoop1);
            wishes.add(opWoop2);
            wishes.add(opWoop3);

            // Initialize plannedPresentations list (to be filled later during scheduling).
            Student student = new Student(wishes, "Salsa" + i, "Boy" + i, "10A", i);
            students.add(student);
        }

        return new BoomData(rooms, companies, students);
    }

    /**
     * Checks that every plannedPresentation gets a timeslot and room assigned
     */
    @Test
    public void testRoomAndTimeslotManagement() {
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        BoomData testBoomData = createTestBoomData();
        roomManagementUnit.execute(testBoomData);

        boolean everyPresentationPlanned = true;

        for (Student student : testBoomData.getStudents()) {
            for (PlannedPresentation plannedPresentation : student.getPlannedPresentations()) {
                if (!Character.isDefined(plannedPresentation.getTimeslot()) || plannedPresentation.getRoom() == null) {
                    everyPresentationPlanned = false;
                    break;
                }
            }
        }

        assertTrue(everyPresentationPlanned);
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
