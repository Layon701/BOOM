package itf221.gvi.boom.data;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all relevant BOOM data.
 */
@AllArgsConstructor
@Data
public class BoomData {
    private List<Room> rooms;
    private List<Company> companies;
    private List<Student> students;

    @Override
    public String toString() {
        return "BoomData{" +
                "rooms=" + rooms +
                ", companies=" + companies +
                ", students=" + students +
                '}';
    }

    /**
     * Collects all planned presentations from the companies.
     */
    public List<PlannedPresentation> getAllPlannedPresentations() {
        List<PlannedPresentation> presentations = new ArrayList<>();
        for (OfferedPresentation offered : getAllOfferedPresentations()) {
            presentations.addAll(offered.getPlannedPresentations());
        }
        return presentations;
    }

    /**
     * Collects all offered presentations from the companies.
     */
    public List<OfferedPresentation> getAllOfferedPresentations() {
        List<OfferedPresentation> offeredPresentations = new ArrayList<>();
        for (Company company : getCompanies()) {
            offeredPresentations.addAll(company.getOfferedPresentations());
        }
        return offeredPresentations;
    }
}
