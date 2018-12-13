package model;

public class SearchingModel implements model.interfaces.SearchModel {
    private String Searching, Tipo;

    public SearchingModel(String Searching, String Tipo) {
        setSearching(Searching);
        setTipo(Tipo);
    }

    public SearchingModel() {
        this(null, null);
    }

    public String getSearching() {
        return Searching;
    }

    public void setSearching(String Searching) {
        this.Searching = Searching;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

}