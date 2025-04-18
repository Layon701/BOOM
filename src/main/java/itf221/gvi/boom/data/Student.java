package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds Student data.
 */
@AllArgsConstructor
@Data
public class Student {
    private List<PlannedPresentation> plannedPresentations;
    private List<OfferedPresentation> wishedPresentations;
    private String surname;
    private String name;
    private String schoolClass;
    private int id;

    public Student(List<OfferedPresentation> wishedPresentations, String surname, String name, String schoolClass, int id) {
        this.plannedPresentations = new ArrayList<>();
        this.wishedPresentations = wishedPresentations;
        this.surname = surname;
        this.name = name;
        this.schoolClass = schoolClass;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "plannedPresentations=" + plannedPresentations.stream().map(PlannedPresentation::getOfferedPresentation).map(OfferedPresentation::getId).toList() +
                ", wishedPresentations=" + wishedPresentations.stream().map(OfferedPresentation::getId).toList() +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", schoolClass='" + schoolClass + '\'' +
                ", id=" + id +
                '}';
    }
}
