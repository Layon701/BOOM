package itf221.gvi.boom.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import itf221.gvi.boom.data.Company;
import itf221.gvi.boom.data.OfferedPresentation;

/**
 * Interpreter class to extract company data from raw input data and create a list of companies
 */
public class CompanyInterpreter {
	public static List<Company> interpret(List<List<String>> data)
	{
		HashMap<String, Company> commap = new HashMap<String, Company>();

		for(List<String> ls : data)
		{	
			if(ls.size() != 6)
			{
				continue;
			}

			String name = ls.get(1);
			Company currentCompany;

			if(commap.containsKey(name))
			{
				currentCompany = commap.get(name);
			}
			else
			{
				currentCompany =new Company(name, new ArrayList<OfferedPresentation>());
				commap.put(name, currentCompany);
			}

			//adds new OfferedPresentation to company
			currentCompany.getOfferedPresentations().add(new OfferedPresentation(Integer.parseInt(ls.get(0)),0,Integer.parseInt(ls.get(3)),ls.get(2),ls.get(5).charAt(0), name));
		}

		List<Company> companies = new ArrayList<Company>(commap.values());
		return companies;
	}
}
