package services;

import model.SearchingModel;
import model.interfaces.HeyBean;
import model.interfaces.SearchModel;

import java.util.ArrayList;
import java.util.List;


public class DetailsService implements services.interfaces.SearchService {

    public DetailsService() { }

    @Override
    public List<Object> search(SearchModel query) {
        if (query instanceof SearchingModel) {
            String[] respostas;
            List<Object> results = new ArrayList<Object>();
            System.out.println(((SearchingModel) query).getTipo());
            System.out.println(((SearchingModel) query).getSearching());
            respostas = HeyBean.GetDetails(((SearchingModel) query).getSearching(), ((SearchingModel) query).getTipo());
            if (respostas[0] == "none") return null;
            else {
                for (String s : respostas) {
                    results.add(s);
                }
                return results;
            }
        }
        return null;
    }
}
