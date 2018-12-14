package model;

public class GetDetailsModel implements model.interfaces.SearchModel {
    private String alvo;

    public GetDetailsModel(String alvo) {
        setAlvo(alvo);
    }

    public GetDetailsModel() {
        this(null);
    }

    public String getAlvo() {
        return alvo;
    }

    public void setAlvo(String alvo) {
        this.alvo = alvo;
    }

}