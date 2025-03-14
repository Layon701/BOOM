package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data for a company's offered presentations. Acts as a presentation blueprint for itf221.gvi.boom.data.PlannedPresentations.
 */
@AllArgsConstructor
@Data
public class OfferedPresentation {
    private int id;
    private int minCapacity;
    private int maxCapacity;
    private String specialty;
    private char earliestTime;
    private String companyName;
    private List<PlannedPresentation> plannedPresentations;

    public OfferedPresentation(int id, int minCapacity, int maxCapacity, String specialty, char earliestTime,  String companyName)
    {
        this.id = id;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.specialty = specialty;
        this.earliestTime = earliestTime;
        this.companyName = companyName;
        this.plannedPresentations = new ArrayList<PlannedPresentation>();
    }

    public String getTitle() {
        return String.format("%s: %s", this.getCompanyName(), this.getSpecialty());
    }
}

