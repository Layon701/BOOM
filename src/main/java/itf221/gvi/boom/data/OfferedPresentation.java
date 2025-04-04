package itf221.gvi.boom.data;

import lombok.*;

import java.util.Comparator;
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

    /**
     * @return the planned presentation with the least amount of attendees.
     * Null check to prevent NullPointerException.
     */
    public PlannedPresentation getPlannedPresentationWithLeastAmountOfAttendees() {
        return plannedPresentations == null ? null : plannedPresentations.stream().min(Comparator.comparingInt(p -> p.getAttendees().size())).orElse(null);
    }

    public String getTitle() {
        return String.format("%s: %s", this.getCompanyName(), this.getSpecialty());
    }


    @Override
    public String toString() {
        return "OfferedPresentation{" +
                "id=" + id +
                ", minCapacity=" + minCapacity +
                ", maxCapacity=" + maxCapacity +
                ", specialty='" + specialty + '\'' +
                ", earliestTime=" + earliestTime +
                ", companyName='" + companyName + '\'' +
                ", plannedPresentations=" + plannedPresentations +
                ", amountOfPresentations=" + amountOfPresentations +
                '}';
        // return String.valueOf(id);
    }
}

