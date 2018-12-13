package action;

import com.opensymphony.xwork2.ActionSupport;
import model.interfaces.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import services.DetailsService;
import services.interfaces.SearchService;

import java.util.ArrayList;
import java.util.List;

public class SearchAction extends ActionSupport {

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
        setResults(getSearchService().search(getInputObject()));
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

}
