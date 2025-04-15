package com.example.socialapp;

public class Post {
    public String autor;
    public String conteudo;
    public int imagemResId;

    public Post(String autor, String conteudo, int imagemResId) {
        this.autor = autor;
        this.conteudo = conteudo;
        this.imagemResId = imagemResId;
    }
}
