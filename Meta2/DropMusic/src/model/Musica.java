package model;

import model.interfaces.SearchModel;

public class Musica implements SearchModel {
    private String Music_name;

    public Musica(String Music_name) {
        setMusicName(Music_name);
    }

    public Musica() {
        this(null);
    }

    public String getMusicName() {
        return Music_name;
    }

    public void setMusicName(String Music_name) {
        this.Music_name = Music_name;
    }
}