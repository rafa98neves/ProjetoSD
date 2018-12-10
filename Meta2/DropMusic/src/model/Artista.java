package model;

import model.interfaces.SearchModel;

public class Artista implements SearchModel {
    private String Nome;

    public Artista(String Nome) {
        setArtistName(Nome);
    }

    public Artista() {
        this(null);
    }

    public String getArtista() {
        return Nome;
    }

    public void setArtistName(String Artista) {
        this.Nome = Artista;
    }
}