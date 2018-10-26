package com.company;

class User{
    private String nome;
    protected String password;
    private boolean editor;
    private int ID;
    User(String nome,String password, boolean editor, int id){
        this.nome = nome;
        this.password = password;
        this.editor = editor;
        this.ID = id;
    }
    public boolean IsEditor(){
        return editor;
    }
	public void ChangeUserToEditor(boolean change){
        this.editor = change;
    }
    public String GetNome(){
        return nome;
    }
    public String GetID(){
        return Integer.toString(ID);
    }
}