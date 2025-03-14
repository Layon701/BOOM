package itf221.gvi.boom.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;
import itf221.gvi.boom.data.Room;
import itf221.gvi.boom.exceptions.InterpretException;

/**
 * Interpreter class to extract company data from raw input data and create a list of companies
 */
public class CompanyInterpreter {
    public static List<Company> interpret(List<List<String>> data) {
        Map<String, Company> commap = new HashMap<String, Company>();

        for (List<String> row : data) {
            if (row.size() != 6) {
                continue;
            }

            String name = row.get(1);
            Company currentCompany;

            if (commap.containsKey(name)) {
                currentCompany = commap.get(name);
            } else {
                currentCompany = new Company(name, new ArrayList<OfferedPresentation>());
                commap.put(name, currentCompany);
            }


            try {
                int id = Integer.parseInt(row.get(0));
                int minCapacity = 0;
                int maxCapacity = Integer.parseInt(row.get(3));
                String specialty = row.get(2);
                char earliestTime = row.get(5).charAt(0);

                currentCompany.getOfferedPresentations().add(new OfferedPresentation(id, minCapacity, maxCapacity, specialty, earliestTime, name));
            } catch (IndexOutOfBoundsException e) {
                throw new InterpretException("Error while trying to get indexes for Company object", e);
            }
        }

        return new ArrayList<Company>(commap.values());
    }
}
