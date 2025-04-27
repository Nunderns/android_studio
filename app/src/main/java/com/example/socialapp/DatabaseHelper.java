package com.example.socialapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "status TEXT," +  // exemplo: 'pendente', 'aceita'
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
}
