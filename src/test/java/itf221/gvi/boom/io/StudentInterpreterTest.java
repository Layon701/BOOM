package itf221.gvi.boom.io;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.Room;
import itf221.gvi.boom.data.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentInterpreterTest {

    /**
     * Tests the interpret method from the RoomInterpreter class.
     */
    @Test
    void interpretTest() {

        // initializer list by scheme: id, minCapacity, maxCapacity, specialty, maxPresentations, earliestTimeslot, name
        Company tmpCompany1 = new Company("Company1", Arrays.asList(new OfferedPresentation(1, 0, 20, "specialty1", 'A', "Company1")));
        Company tmpCompany2 = new Company("Company2", Arrays.asList(new OfferedPresentation(2, 0, 20, "specialty2", 'A', "Company2")));
        Company tmpCompany3 = new Company("Company3", Arrays.asList(new OfferedPresentation(3, 0, 30, "specialty3", 'A', "Company3")));
        Company tmpCompany4 = new Company("Company4", Arrays.asList(new OfferedPresentation(4, 0, 5, "specialty4", 'C', "Company4")));
        Company tmpCompany5 = new Company("Company5", Arrays.asList(new OfferedPresentation(5, 0, 5, "specialty5", 'C', "Company5")));

        Map<String, Company> tmpCommap = Map.of("Company1", tmpCompany1, "Company2", tmpCompany2, "Company3", tmpCompany3, "Company4", tmpCompany4, "Company5", tmpCompany5);

        List<Company> companyData = new ArrayList<Company>(tmpCommap.values());

        List<OfferedPresentation> allOffered = new ArrayList<OfferedPresentation>();
        allOffered.addAll(tmpCompany1.getOfferedPresentations());
        allOffered.addAll(tmpCompany2.getOfferedPresentations());
        allOffered.addAll(tmpCompany3.getOfferedPresentations());
        allOffered.addAll(tmpCompany4.getOfferedPresentations());
        allOffered.addAll(tmpCompany5.getOfferedPresentations());

        Student tmpStudent1 = new Student(allOffered, "surname1", "name1", "Class1", 1);
        Student tmpStudent2 = new Student(allOffered, "surname2", "name2", "Class2", 2);
        Student tmpStudent3 = new Student(allOffered, "surname3", "name3", "Class3", 3);
        Student tmpStudent4 = new Student(allOffered, "surname4", "name4", "Class4", 4);

        List<Student> expectedData = Arrays.asList(tmpStudent1, tmpStudent2, tmpStudent3, tmpStudent4);


        ArrayList<String> tmpRow1 = new ArrayList<String>(Arrays.asList("schoolclass1", "name1", "surname1", "1", "2", "3", "4", "5"));
        ArrayList<String> tmpRow2 = new ArrayList<String>(Arrays.asList("schoolclass2", "name2", "surname2", "1", "2", "3", "4", "5"));
        ArrayList<String> tmpRow3 = new ArrayList<String>(Arrays.asList("schoolclass3", "name3", "surname3", "1", "2", "3", "4", "5"));
        ArrayList<String> tmpRow4 = new ArrayList<String>(Arrays.asList("schoolclass4", "name4", "surname4", "1", "2", "3", "4", "5"));

        List<List<String>> testParameters = new ArrayList<>(Arrays.asList(tmpRow1, tmpRow2, tmpRow3));

        List<Student> testData = StudentInterpreter.interpret(testParameters, companyData);
        assertEquals(expectedData, testData);

    }
}
