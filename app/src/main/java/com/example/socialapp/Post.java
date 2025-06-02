package com.example.socialapp;

public class Post {
    private int id;     
    private int idUsuario;    // ID do usuário que criou o post
    private String autor;
    private String conteudo;
    private int imagemResId;
    private int comentarios;
    private int curtidas;
    private int favoritos;

    // Construtor atualizado
    public Post(int id, int idUsuario, String autor, String conteudo, int imagemResId, int comentarios, int curtidas, int favoritos) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.autor = autor;
        this.conteudo = conteudo;
        this.imagemResId = imagemResId;
        this.comentarios = comentarios;
        this.curtidas = curtidas;
        this.favoritos = favoritos;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

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
