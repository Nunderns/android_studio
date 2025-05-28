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

        // Tabela de Comentários (agora com idComentarioPai)
        db.execSQL("CREATE TABLE comentarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPostagem INTEGER," +
                "idUsuario INTEGER," +
                "comentario TEXT," +
                "data TEXT," +
                "idComentarioPai INTEGER," +
                "FOREIGN KEY(idPostagem) REFERENCES postagens(id)," +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id)," +
                "FOREIGN KEY(idComentarioPai) REFERENCES comentarios(id)" +
                ");");

        // Tabela de Curtidas
        db.execSQL("CREATE TABLE curtidas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idPostagem INTEGER," +
                "idUsuario INTEGER," +
                "FOREIGN KEY(idPostagem) REFERENCES postagens(id)," +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id)" +
                ");");

        // Tabela de Favoritos
        db.execSQL("CREATE TABLE favoritos (" +
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
                int postId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int comentarios = contarComentarios(postId);
                int curtidas = contarCurtidas(postId);
                int favoritos = contarFavoritos(postId);

                Post post = new Post(
                        postId, // ID do post
                        cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")),      // ID do usuário que postou
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),        // Nome do autor
                        cursor.getString(cursor.getColumnIndexOrThrow("conteudo")),    // Conteúdo do post
                        R.drawable.ic_launcher_background,                            // Imagem (você pode trocar depois)
                        comentarios, // Comentários
                        curtidas,    // Curtidas
                        favoritos    // Favoritos
                );
                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return postList;
    }

    // Buscar usuário pelo nome
    public Cursor buscarUsuarioPorNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE nome = ?", new String[]{nome});
    }

    // Buscar posts de um usuário específico
    public List<Post> buscarPostsPorAutor(String nomeAutor) {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
            "SELECT p.id, p.idUsuario, u.nome, p.conteudo, p.imagem, p.data " +
            "FROM postagens p " +
            "INNER JOIN usuarios u ON p.idUsuario = u.id " +
            "WHERE u.nome = ? " +
            "ORDER BY p.id DESC",
            new String[]{nomeAutor}
        );

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    cursor.getString(cursor.getColumnIndexOrThrow("conteudo")),
                    R.drawable.ic_launcher_background,
                    0, 0, 0
                );
                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return postList;
    }

    // Inserir comentário (pode ser resposta)
    public void inserirComentario(int idPostagem, int idUsuario, String comentario, String data, Integer idComentarioPai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPostagem", idPostagem);
        values.put("idUsuario", idUsuario);
        values.put("comentario", comentario);
        values.put("data", data);
        if (idComentarioPai != null) {
            values.put("idComentarioPai", idComentarioPai);
        } else {
            values.putNull("idComentarioPai");
        }
        db.insert("comentarios", null, values);
        db.close();
    }

    // Buscar comentários de um post (apenas os principais, sem pai)
    public Cursor buscarComentariosPrincipais(int idPostagem) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT c.*, u.nome FROM comentarios c INNER JOIN usuarios u ON c.idUsuario = u.id WHERE c.idPostagem = ? AND c.idComentarioPai IS NULL ORDER BY c.data ASC", new String[]{String.valueOf(idPostagem)});
    }

    // Buscar respostas de um comentário
    public Cursor buscarRespostasComentario(int idComentarioPai) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT c.*, u.nome FROM comentarios c INNER JOIN usuarios u ON c.idUsuario = u.id WHERE c.idComentarioPai = ? ORDER BY c.data ASC", new String[]{String.valueOf(idComentarioPai)});
    }

    // Curtir post
    public void curtirPost(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPostagem", idPostagem);
        values.put("idUsuario", idUsuario);
        db.insert("curtidas", null, values);
        db.close();
    }

    // Descurtir post
    public void descurtirPost(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("curtidas", "idPostagem = ? AND idUsuario = ?", new String[]{String.valueOf(idPostagem), String.valueOf(idUsuario)});
        db.close();
    }

    // Verificar se usuário já curtiu
    public boolean usuarioCurtiu(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM curtidas WHERE idPostagem = ? AND idUsuario = ?", new String[]{String.valueOf(idPostagem), String.valueOf(idUsuario)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Contar curtidas
    public int contarCurtidas(int idPostagem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM curtidas WHERE idPostagem = ?", new String[]{String.valueOf(idPostagem)});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Favoritar post
    public void favoritarPost(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idPostagem", idPostagem);
        values.put("idUsuario", idUsuario);
        db.insert("favoritos", null, values);
        db.close();
    }

    // Desfavoritar post
    public void desfavoritarPost(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favoritos", "idPostagem = ? AND idUsuario = ?", new String[]{String.valueOf(idPostagem), String.valueOf(idUsuario)});
        db.close();
    }

    // Verificar se usuário já favoritou
    public boolean usuarioFavoritou(int idPostagem, int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM favoritos WHERE idPostagem = ? AND idUsuario = ?", new String[]{String.valueOf(idPostagem), String.valueOf(idUsuario)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Contar favoritos
    public int contarFavoritos(int idPostagem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM favoritos WHERE idPostagem = ?", new String[]{String.valueOf(idPostagem)});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Contar comentários
    public int contarComentarios(int idPostagem) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM comentarios WHERE idPostagem = ?", new String[]{String.valueOf(idPostagem)});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Método para verificar o login do usuário
    public int verificarLogin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE email = ? AND senha = ?", new String[]{email, senha});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

}
