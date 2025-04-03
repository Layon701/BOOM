package itf221.gvi.boom.io;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.Student;
import itf221.gvi.boom.exceptions.InterpretException;

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
            if (row.size() != 9) {
                continue;
            }

            boolean hasEmpty = false;
            for (String cell : row) {
                if (cell.trim().isEmpty()) {
                    hasEmpty = true;
                    break;
                }
            }
            if (hasEmpty) {
                continue;
            }

            List<OfferedPresentation> wishes = new ArrayList<OfferedPresentation>();
            for (int columnIndex = 3; columnIndex < 9; columnIndex++) {
                try {
                    Integer wishIndex = (int) Double.parseDouble(row.get(columnIndex));
                    OfferedPresentation presentation = presentationMap.get(wishIndex);
                    wishes.add(presentation);
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    throw new InterpretException("Error while trying to get wishes for Student object", e);
                }
            }

            try {
                String schoolClass = row.get(0);
                String name = row.get(1);
                String surname = row.get(2);

                students.add(new Student(wishes, surname, name, schoolClass, studentId));
            } catch (IndexOutOfBoundsException e) {
                throw new InterpretException("Error while trying to get indexes for Student object", e);
            }
        }
        System.out.println("Success creating students from student data");
        return students;
    }
}
