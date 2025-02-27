package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Holds Student data.
 */
@AllArgsConstructor
@Data
public class Student {
    private List<PlannedPresentation> plannedPresentations;
    private List<OfferedPresentation> offeredPresentations;
    private String surname;
    private String name;
    private String schoolClass;
    private int id;
}
