package itf221.gvi.boom.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Holds Company data.
 */
@AllArgsConstructor
@Data
public class Company {
    String name;
    OfferedPresentation offeredPresentations;
}
