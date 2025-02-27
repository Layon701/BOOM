package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Holds Company data.
 */
@AllArgsConstructor
@Data
public class Company {
    private String name;
    private List<OfferedPresentation> offeredPresentations;
}
