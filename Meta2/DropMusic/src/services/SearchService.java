package services;

import model.SearchingModel;
import model.interfaces.HeyBean;
import model.interfaces.SearchModel;

import java.util.ArrayList;
import java.util.List;


public class SearchService implements services.interfaces.SearchService {

    public SearchService() { }

    @Override
    public List<Object> search(SearchModel query) {
        if (query instanceof SearchingModel) {
            String[] results_aux;
            List<Object> results = new ArrayList<Object>();

            //Procurar Musicas
            results.add("#musicas");
            results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(),"musica");
            for(String s:results_aux) results.add(s);

            //Procurar Artistas
            results.add("#artistas");
            results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(),"musica");
            for(String s:results_aux) results.add(s);

            //Procurar Albuns
            results.add("#albuns");
            results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(),"musica");
            for(String s:results_aux) results.add(s);
            return results;
        }
        return null;
    }
}
