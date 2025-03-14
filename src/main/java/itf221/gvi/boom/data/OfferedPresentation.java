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
    @NonNull
    private List<PlannedPresentation> plannedPresentations;

    private int amountOfPresentations;

    public String getTitle() {
        return String.format("%s: %s", this.getCompanyName(), this.getSpecialty());
    }
}

