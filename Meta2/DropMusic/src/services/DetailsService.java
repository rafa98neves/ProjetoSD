package services;

import model.GetDetailsModel;
import model.SearchingModel;
import model.interfaces.HeyBean;
import model.interfaces.SearchModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsService implements services.interfaces.SearchService{
    public DetailsService() { }

    @Override
    public List<Object> search(SearchModel query, Map<String, Object> Session) {
        if (query instanceof GetDetailsModel) {
            String[] respostas;

            List<Object> results = new ArrayList<Object>();
            respostas = HeyBean.GetDetails((String) Session.get("ID"),((GetDetailsModel) query).getAlvo(),(String) Session.get("LastSearchType"));
            Session.put("LastSearched",((GetDetailsModel) query).getAlvo());

            if (respostas[0].compareTo("none")==0) return null;
            else {
                for(int i=0;i<respostas.length;i++) {
                    String s = respostas[i];
                    if(s.compareTo("Critica")==0){
                        s = "Utilizador " + respostas[i+1] + " pontuou o album com " + respostas[i+2] + " pontos e fez a critica: "+respostas[i+3];
                        results.add(s);
                        i+=3;
                        results.add(" ");
                    }
                    else {
                        results.add(s);
                    }
                }
                return results;
            }
        }
        return null;
    }


}
