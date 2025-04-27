package com.example.socialapp;

public class Post {
    public String autor;
    public String conteudo;
    public int imagemResId;
    public int comentarios;
    public int curtidas;
    public int favoritos;

    public Post(String autor, String conteudo, int imagemResId, int comentarios, int curtidas, int favoritos) {
        this.autor = autor;
        this.conteudo = conteudo;
        this.imagemResId = imagemResId;
        this.comentarios = comentarios;
        this.curtidas = curtidas;
        this.favoritos = favoritos;
    }

    // Getters que estavam faltando
    public String getAutor() {
        return autor;
    }

    public String getConteudo() {
        return conteudo;
    }

    public int getImagemResId() {
        return imagemResId;
    }

    public int getComentarios() {
        return comentarios;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public int getFavoritos() {
        return favoritos;
    }
}
