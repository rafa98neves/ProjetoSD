package model;

import model.interfaces.SearchModel;

public class User implements SearchModel {
    private String Username;
    protected String Password;
    public User(String username, String password) {
        this.Password = password;
        this.Username = username;
    }

    public User() {
        this(null, null);
    }

    public String getUsername() {
        return Username;
    }
}