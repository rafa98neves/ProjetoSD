package model;

public class GetDetailsModel implements model.interfaces.SearchModel {
    private String alvo, tipo;

    public GetDetailsModel(String alvo, String tipo) {
        setAlvo(alvo);
        setTipo(tipo);
    }

    public GetDetailsModel() {
        this(null, null);
    }

    public String getAlvo() {
        return alvo;
    }

    public void setAlvo(String alvo) {
        this.alvo = alvo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}