package com.example.socialapp;

public class User {
    private int id;
    private String nome;
    private String fotoPerfil;

    public User(int id, String nome, String fotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.fotoPerfil = fotoPerfil;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

}
