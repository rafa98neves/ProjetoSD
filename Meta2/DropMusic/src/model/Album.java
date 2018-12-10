package model;

import model.interfaces.SearchModel;

public class Album implements SearchModel {
    private String Nome;
    private String Artista;
    private Musica[] Musicas;
    public Album(String Nome, String Artista) {
        setAlbumName(Nome);
        setArtistName(Artista);
    }

    public Album() {
        this(null, null);
    }

    public String getAlbumName() {
        return Nome;
    }

    public void setAlbumName(String Nome) {
        this.Nome = Nome;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtistName(String Artista) {
        this.Artista = Artista;
    }

    public String[] getMusicsName() {
        int index = 0;
        String[] nomes = new String[Musicas.length];
        for( Musica M : Musicas){
            nomes[index] = M.getMusicName();
            index++;
        }
        return nomes;
    }

}