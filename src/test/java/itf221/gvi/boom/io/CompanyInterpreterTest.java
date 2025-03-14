package itf221.gvi.boom.io;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CompanyInterpreterTest {

    private class CompanyComparator implements Comparator<Company> {
        @Override
        public int compare(Company c1, Company c2) {
            return c1.getName().compareTo(c2.getName());
        }
    }

    /**
     * Tests the interpret method from the CompanyInterpreter class.
     */
    @Test
    void interpretTest() {

        // initializer list by scheme: id, minCapacity, maxCapacity, specialty, maxPresentations, earliestTimeslot, name
        Company tmpCompany1 = new Company("Company1", Arrays.asList(new OfferedPresentation(1, 0, 20, "specialty1", 'A', "Company1")));
        Company tmpCompany2 = new Company("Company2", Arrays.asList(new OfferedPresentation(2, 0, 20, "specialty2", 'A', "Company2")));
        Company tmpCompany3 = new Company("Company3", Arrays.asList(new OfferedPresentation(3, 0, 30, "specialty3", 'A', "Company3")));
        Company tmpCompany4 = new Company("Company4", Arrays.asList(new OfferedPresentation(4, 0, 5, "specialty4", 'C', "Company4")));

        Map<String, Company> tmpCommap = Map.of("Company1", tmpCompany1, "Company2", tmpCompany2, "Company3", tmpCompany3, "Company4", tmpCompany4);

        List<Company> expectedData = new ArrayList<Company>(tmpCommap.values());
        Collections.sort(expectedData, new CompanyComparator());

        // initializer list by scheme: id, name, specialty, maxCapacity, maxPresentations, earliestTimeslot
        ArrayList<String> tmpRow1 = new ArrayList<String>(Arrays.asList("1", "Company1", "specialty1", "20", "5", "A"));
        ArrayList<String> tmpRow2 = new ArrayList<String>(Arrays.asList("2", "Company2", "specialty2", "20", "5", "A"));
        ArrayList<String> tmpRow3 = new ArrayList<String>(Arrays.asList("3", "Company3", "specialty3", "30", "2", "A"));
        ArrayList<String> tmpRow4 = new ArrayList<String>(Arrays.asList("4", "Company4", "specialty4", "5", "2", "C"));

        List<List<String>> testParameters = new ArrayList<>(Arrays.asList(tmpRow1, tmpRow2, tmpRow3, tmpRow4));

        List<Company> testData = CompanyInterpreter.interpret(testParameters);
        Collections.sort(testData, new CompanyComparator());
        assertEquals(expectedData, testData);
    }
}