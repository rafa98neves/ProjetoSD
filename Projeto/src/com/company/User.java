package com.company;

class User{
    private String nome;
    protected String password;
    private boolean editor;
    User(String nome,String password){
        this.nome = nome;
        this.password = password;
        this.editor = false;
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
}