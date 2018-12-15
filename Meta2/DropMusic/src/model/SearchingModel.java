package model;

public class SearchingModel implements model.interfaces.SearchModel {
    private String Searching, Tipo, Flag;

    public SearchingModel(String Searching, String Tipo, String Flag) {
        setSearching(Searching);
        setTipo(Tipo);
        setFlag(Flag);
    }

    public SearchingModel() {
        this(null, null, null);
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

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String Flag) {
        this.Flag = Flag;
    }
}