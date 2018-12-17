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
