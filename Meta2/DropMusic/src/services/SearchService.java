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
            if(((SearchingModel) query).getTipo().compareTo("musica")==0)
                results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(), "musica");

            //Procurar Artistas
            else if(((SearchingModel) query).getTipo().compareTo("artista")==0)
                results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(), "artista");

            //Procurar Albuns
            else if(((SearchingModel) query).getTipo().compareTo("album")==0)
            results_aux = HeyBean.Procura(((SearchingModel) query).getSearching(),"album");

            else return null;

            for(String s:results_aux) results.add(s);
            return results;
        }
        return null;
    }
}
