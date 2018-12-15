package services;

import com.opensymphony.xwork2.ActionSupport;
import model.SearchingModel;
import model.interfaces.HeyBean;
import model.interfaces.SearchModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchService implements services.interfaces.SearchService {

    public SearchService() { }
    @Override
    public List<Object> search(SearchModel query, Map<String, Object> session) {
        if (query instanceof SearchingModel) {
            String[] results_aux;
            List<Object> results = new ArrayList<Object>();
            //Procurar Musicas
            if(((SearchingModel) query).getTipo().compareTo("musica")==0) {
                session.put("LastSearchType","musica");
                System.out.println(((SearchingModel) query).getFalg());
                if(((SearchingModel) query).getFalg().compareTo("upload")==0) results.add("TRUE");
                results_aux = HeyBean.Procura((String) session.get("ID"), ((SearchingModel) query).getSearching(), "musica");
            }
            //Procurar Artistas
            else if(((SearchingModel) query).getTipo().compareTo("artista")==0){
                session.put("LastSearchType","artista");
                results_aux = HeyBean.Procura((String) session.get("ID"),((SearchingModel) query).getSearching(), "artista");
            }

            //Procurar Albuns
            else if(((SearchingModel) query).getTipo().compareTo("album")==0){
                session.put("LastSearchType","album");
                results_aux = HeyBean.Procura((String) session.get("ID"),((SearchingModel) query).getSearching(),"album");
            }
            else return null;

            for(String s:results_aux) results.add(s);
            return results;
        }
        return null;
    }

}
