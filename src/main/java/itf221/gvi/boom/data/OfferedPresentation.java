package itf221.gvi.boom.data;

import lombok.*;

import java.util.List;

/**
 * Holds data for a company's offered presentations. Acts as a presentation blueprint for itf221.gvi.boom.data.PlannedPresentations.
 */
@RequiredArgsConstructor
@Data
public class OfferedPresentation {
    @NonNull
    private int id;
    @NonNull
    private int minCapacity;
    @NonNull
    private int maxCapacity;
    @NonNull
    private String specialty;
    @NonNull
    private char earliestTime;
    @NonNull
    private String companyName;

    private List<PlannedPresentation> plannedPresentations;

    /**
     * Defines how many plannedPresentations should be instantiated.
     */
    private int amountOfPresentations;

    public PlannedPresentation getPlannedPresentationWithLeastAmountOfAtandees(){
        return plannedPresentations.getFirst();
    }

    public String getTitle() {
        return String.format("%s: %s", this.getCompanyName(), this.getSpecialty());
    }
}

