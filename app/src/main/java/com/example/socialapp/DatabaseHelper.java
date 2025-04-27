package com.example.socialapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "socialapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela de Usuários
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "senha TEXT NOT NULL," +
                "bio TEXT," +
                "fotoPerfil TEXT" +
                ");");

        // Tabela de Postagens
        db.execSQL("CREATE TABLE postagens (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario INTEGER," +
                "conteudo TEXT," +
                "imagem TEXT," +
                "data TEXT," +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id)" +
                ");");

        // Tabela de Amizades
        db.execSQL("CREATE TABLE amizades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idUsuario1 INTEGER," +
                "idUsuario2 INTEGER," +
                "status TEXT," +
                "FOREIGN KEY(idUsuario1) REFERENCES usuarios(id)," +
                "FOREIGN KEY(idUsuario2) REFERENCES usuarios(id)" +
                ");");

        // Tabela de Comentários
        db.execSQL("CREATE TABLE comentarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPostagem INTEGER," +
                "idUsuario INTEGER," +
                "comentario TEXT," +
                "data TEXT," +
                "FOREIGN KEY(idPostagem) REFERENCES postagens(id)," +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id)" +
                ");");

        // Tabela de Curtidas
        db.execSQL("CREATE TABLE curtidas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPostagem INTEGER," +
                "idUsuario INTEGER," +
                "FOREIGN KEY(idPostagem) REFERENCES postagens(id)," +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS curtidas");
        db.execSQL("DROP TABLE IF EXISTS comentarios");
        db.execSQL("DROP TABLE IF EXISTS amizades");
        db.execSQL("DROP TABLE IF EXISTS postagens");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public void excluirPost(int idPost) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("postagens", "id = ?", new String[]{String.valueOf(idPost)});
        db.close();
    }


    // Função para inserir um post novo
    public void inserirPost(int idUsuario, String conteudo, String imagem, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idUsuario", idUsuario);
        values.put("conteudo", conteudo);
        values.put("imagem", imagem);
        values.put("data", data);
        db.insert("postagens", null, values);
        db.close();
    }

    // Função para buscar todos os posts (Feed)
    public List<Post> buscarTodosPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT p.id, p.idUsuario, u.nome, p.conteudo, p.imagem, p.data " +
                        "FROM postagens p " +
                        "INNER JOIN usuarios u ON p.idUsuario = u.id " +
                        "ORDER BY p.id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),            // ID do post
                        cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")),      // ID do usuário que postou
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),        // Nome do autor
                        cursor.getString(cursor.getColumnIndexOrThrow("conteudo")),    // Conteúdo do post
                        R.drawable.ic_launcher_background,                            // Imagem (você pode trocar depois)
                        0, // Comentários (não estamos buscando ainda)
                        0, // Curtidas
                        0  // Favoritos
                );
                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return postList;
    }

}
