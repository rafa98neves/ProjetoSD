package model;

import model.interfaces.SearchModel;

public class Musica implements SearchModel {
    private String Nome;
    private String Artista;

    public Musica(String Nome, String Artista) {
        setMusicName(Nome);
        setArtistName(Artista);
    }

    public Musica() {
        this(null, null);
    }

    public String getMusicName() {
        return Nome;
    }

    public void setMusicName(String Nome) {
        this.Nome = Nome;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtistName(String Artista) {
        this.Artista = Artista;
    }
}