package services;

import model.Musica;
import model.interfaces.HeyBean;
import model.interfaces.SearchModel;
import services.interfaces.SearchService;

import java.util.ArrayList;
import java.util.List;


public class MusicService implements SearchService {

    public MusicService() { }

    @Override
    public List<Object> search(SearchModel query) {
        if (query instanceof Musica) {
            String[] results_aux;
            List<Object> results = new ArrayList<Object>();
            System.out.println("Ola, " + ((Musica) query).getMusicName());
            results_aux = HeyBean.Procura(((Musica) query).getMusicName(),"musica");
            for(String s:results_aux) results.add(s);
            System.out.println("Results: " + results);
            return results;
        }
        return null;
    }
}
