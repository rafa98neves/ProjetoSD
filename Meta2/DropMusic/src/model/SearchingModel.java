package model;

public class SearchingModel implements model.interfaces.SearchModel {
    private String Searching;

    public SearchingModel(String Searching) {
        setSearching(Searching);
    }

    public SearchingModel() {
        this(null);
    }

    public String getSearching() {
        return Searching;
    }

    public void setSearching(String Searching) {
        this.Searching = Searching;
    }
}