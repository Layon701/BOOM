package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Student {
    List<PlannedPresentation> plannedPresentations;
    List<OfferedPresentation> offeredPresentations;
    String surname;
    String name;
    String schoolClass;
    int id;
}
