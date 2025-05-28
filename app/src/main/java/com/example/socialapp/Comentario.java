package com.example.socialapp;

public class Comentario {
    private int id;
    private int idPostagem;
    private int idUsuario;
    private String nomeAutor;
    private String texto;
    private String data;
    private Integer idComentarioPai;

    public Comentario(int id, int idPostagem, int idUsuario, String nomeAutor, String texto, String data, Integer idComentarioPai) {
        this.id = id;
        this.idPostagem = idPostagem;
        this.idUsuario = idUsuario;
        this.nomeAutor = nomeAutor;
        this.texto = texto;
        this.data = data;
        this.idComentarioPai = idComentarioPai;
    }

    public int getId() { return id; }
    public int getIdPostagem() { return idPostagem; }
    public int getIdUsuario() { return idUsuario; }
    public String getNomeAutor() { return nomeAutor; }
    public String getTexto() { return texto; }
    public String getData() { return data; }
    public Integer getIdComentarioPai() { return idComentarioPai; }
} 