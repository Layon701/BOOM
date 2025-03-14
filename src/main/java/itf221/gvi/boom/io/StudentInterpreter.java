package itf221.gvi.boom.io;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.Student;

/**
 * Interpreter for the student Excel-sheet
 */
public class StudentInterpreter {
    /**
     * Creates a list of Student objects
     *
     * @param data List<List<String>> (from the Xlsx-Reader)
     * @return List<Student>
     */
    public static List<Student> interpret(List<List<String>> data, List<Company> companies) {
        //make Map of presentations
        Map<Integer, OfferedPresentation> presentationMap = new HashMap<Integer, OfferedPresentation>();
        for (Company company : companies) {
            for (OfferedPresentation presentation : company.getOfferedPresentations()) {
                presentationMap.put(presentation.getId(), presentation);
            }
        }

        //
        ArrayList<Student> students = new ArrayList<Student>();
        int studentId = 0;
        for (List<String> row : data) {
            studentId++;
            if (row.size() != 8) {
                continue;
            }

            List<OfferedPresentation> wishes = new ArrayList<OfferedPresentation>();
            for (int columnIndex = 3; columnIndex < 8; columnIndex++) {
                Integer wishIndex = Integer.parseInt(row.get(columnIndex));
                OfferedPresentation presentation = presentationMap.get(wishIndex);
                wishes.add(presentation);
            }

            String schoolClass = row.get(0);
            String name = row.get(1);
            String surname = row.get(2);

            students.add(new Student(wishes, surname, name, schoolClass, studentId));
        }
        return students;
    }
}
