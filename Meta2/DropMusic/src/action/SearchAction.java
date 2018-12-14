package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.SearchModel;
import org.apache.struts2.interceptor.SessionAware;
import services.interfaces.SearchService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAction extends ActionSupport implements SessionAware{
    private Map<String, Object> session;

    private SearchModel inputObject;

    private SearchService searchService;

    /* Search results to be populated by this action */
    private List<Object> results;

    public SearchAction()
    {
        setResults(new ArrayList<Object>());
    }

    public String execute()
    {
        Map<String, Object> session = ActionContext.getContext().getSession();
        setResults(getSearchService().search(getInputObject(),session));
        return SUCCESS;
    }

    public SearchModel getInputObject() {
        return inputObject;
    }

    public void setInputObject(SearchModel inputObject) { this.inputObject = inputObject; }

    public SearchService getSearchService() {
        return searchService;
    }
    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }


    @Override
    public void setSession(Map<String, Object> map) {
        this.session = session;
    }
}
