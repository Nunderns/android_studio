package com.example.socialapp;

public class User {
    private int id;
    private String nome;
    private String fotoPerfil; // Pode ser um caminho de arquivo, URL ou resource ID

    // Construtor
    public User(int id, String nome, String fotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    // Setters (opcionais, dependendo da necessidade)
    // public void setId(int id) { this.id = id; }
    // public void setNome(String nome) { this.nome = nome; }
    // public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
