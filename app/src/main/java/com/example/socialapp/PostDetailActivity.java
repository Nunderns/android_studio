package com.example.socialapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private TextView txtAutorDetail, txtConteudoDetail, txtComentariosDetail, txtCurtidasDetail, txtFavoritosDetail;
    private ImageView imgPostDetail, btnMaisOpcoes, imgLike, imgBookmark, imgComment;
    private int idPost;
    private int idUsuarioPost;
    private int idUsuarioAtual = 1; // Supondo usuário logado = id 1 (pode ser variável depois)
    private RecyclerView recyclerComentarios;
    private ComentarioAdapter comentarioAdapter;
    private List<Comentario> listaComentarios = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private EditText editComentario;
    private ImageView btnEnviarComentario;
    private Integer idComentarioPaiRespondendo = null;
    private boolean usuarioCurtiu = false;
    private boolean usuarioFavoritou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        txtAutorDetail = findViewById(R.id.txtAutorDetail);
        txtConteudoDetail = findViewById(R.id.txtConteudoDetail);
        txtComentariosDetail = findViewById(R.id.txtComentariosDetail);
        txtCurtidasDetail = findViewById(R.id.txtCurtidasDetail);
        txtFavoritosDetail = findViewById(R.id.txtFavoritosDetail);
        imgPostDetail = findViewById(R.id.imgPostDetail);
        btnMaisOpcoes = findViewById(R.id.btnMaisOpcoes);
        recyclerComentarios = findViewById(R.id.recyclerComentarios);
        editComentario = findViewById(R.id.editComentario);
        btnEnviarComentario = findViewById(R.id.btnEnviarComentario);
        dbHelper = new DatabaseHelper(this);
        imgLike = findViewById(R.id.imgLikeDetail);
        imgBookmark = findViewById(R.id.imgBookmarkDetail);
        imgComment = findViewById(R.id.imgCommentDetail);

        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentarioAdapter = new ComentarioAdapter(this, listaComentarios, comentario -> {
            idComentarioPaiRespondendo = comentario.getId();
            editComentario.setHint("Respondendo a " + comentario.getNomeAutor());
            editComentario.requestFocus();
        });
        recyclerComentarios.setAdapter(comentarioAdapter);

        // Dados do post recebidos
        String autor = getIntent().getStringExtra("autor");
        String conteudo = getIntent().getStringExtra("conteudo");
        int imagemResId = getIntent().getIntExtra("imagemResId", 0);
        int comentarios = getIntent().getIntExtra("comentarios", 0);
        int curtidas = getIntent().getIntExtra("curtidas", 0);
        int favoritos = getIntent().getIntExtra("favoritos", 0);
        idPost = getIntent().getIntExtra("idPost", -1);
        idUsuarioPost = getIntent().getIntExtra("idUsuario", -1);

        // Preencher campos
        txtAutorDetail.setText(autor);
        txtConteudoDetail.setText(conteudo);
        txtComentariosDetail.setText(String.valueOf(comentarios));
        txtCurtidasDetail.setText(String.valueOf(curtidas));
        txtFavoritosDetail.setText(String.valueOf(favoritos));

        if (imagemResId != 0 && imagemResId != R.drawable.ic_launcher_background) {
            imgPostDetail.setVisibility(View.VISIBLE);
            imgPostDetail.setImageResource(imagemResId);
        } else {
            imgPostDetail.setVisibility(View.GONE);
        }

        // Mostrar botão três pontinhos se for dono
        if (idUsuarioAtual == idUsuarioPost) {
            btnMaisOpcoes.setVisibility(View.VISIBLE);
            btnMaisOpcoes.setOnClickListener(v -> abrirMenuOpcoes());
        }

        carregarComentarios();
        atualizarContadores();
        atualizarEstadoLikeFavorito();

        btnEnviarComentario.setOnClickListener(v -> enviarComentario());
        imgLike.setOnClickListener(v -> alternarLike());
        imgBookmark.setOnClickListener(v -> alternarFavorito());
        imgComment.setOnClickListener(v -> {
            editComentario.requestFocus();
            recyclerComentarios.postDelayed(() -> {
                recyclerComentarios.smoothScrollToPosition(listaComentarios.size());
            }, 200);
        });
    }

    private void abrirMenuOpcoes() {
        PopupMenu popupMenu = new PopupMenu(this, btnMaisOpcoes);
        popupMenu.getMenuInflater().inflate(R.menu.menu_post_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                confirmarExclusao();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.excluir))
                .setMessage(getString(R.string.confirmar_exclusao))
                .setPositiveButton(getString(R.string.excluir), (dialog, which) -> deletarPost())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void deletarPost() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (idPost != -1) {
            dbHelper.excluirPost(idPost);
            Toast.makeText(this, getString(R.string.excluido_sucesso), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.erro_excluir), Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarComentarios() {
        listaComentarios.clear();
        // Buscar comentários principais
        try (android.database.Cursor cursor = dbHelper.buscarComentariosPrincipais(idPost)) {
            while (cursor.moveToNext()) {
                Comentario comentario = new Comentario(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("idPostagem")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("comentario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data")),
                        null
                );
                listaComentarios.add(comentario);
                // Buscar respostas
                try (android.database.Cursor c2 = dbHelper.buscarRespostasComentario(comentario.getId())) {
                    while (c2.moveToNext()) {
                        Comentario resposta = new Comentario(
                                c2.getInt(c2.getColumnIndexOrThrow("id")),
                                c2.getInt(c2.getColumnIndexOrThrow("idPostagem")),
                                c2.getInt(c2.getColumnIndexOrThrow("idUsuario")),
                                c2.getString(c2.getColumnIndexOrThrow("nome")),
                                c2.getString(c2.getColumnIndexOrThrow("comentario")),
                                c2.getString(c2.getColumnIndexOrThrow("data")),
                                comentario.getId()
                        );
                        listaComentarios.add(resposta);
                    }
                }
            }
        }
        comentarioAdapter.notifyDataSetChanged();
    }

    private void enviarComentario() {
        String texto = editComentario.getText().toString().trim();
        if (texto.isEmpty()) return;
        // Supondo que idUsuarioAtual está definido corretamente
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        dbHelper.inserirComentario(idPost, idUsuarioAtual, texto, data, idComentarioPaiRespondendo);
        editComentario.setText("");
        editComentario.setHint("Escreva um comentário...");
        idComentarioPaiRespondendo = null;
        carregarComentarios();
    }

    private void atualizarContadores() {
        int curtidas = dbHelper.contarCurtidas(idPost);
        int favoritos = dbHelper.contarFavoritos(idPost);
        int comentarios = listaComentarios.size();
        txtCurtidasDetail.setText(String.valueOf(curtidas));
        txtFavoritosDetail.setText(String.valueOf(favoritos));
        txtComentariosDetail.setText(String.valueOf(comentarios));
    }

    private void atualizarEstadoLikeFavorito() {
        usuarioCurtiu = dbHelper.usuarioCurtiu(idPost, idUsuarioAtual);
        usuarioFavoritou = dbHelper.usuarioFavoritou(idPost, idUsuarioAtual);
        imgLike.setImageResource(usuarioCurtiu ? R.drawable.ic_like_filled : R.drawable.ic_like);
        imgBookmark.setImageResource(usuarioFavoritou ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
    }

    private void alternarLike() {
        if (usuarioCurtiu) {
            dbHelper.descurtirPost(idPost, idUsuarioAtual);
        } else {
            dbHelper.curtirPost(idPost, idUsuarioAtual);
        }
        atualizarContadores();
        atualizarEstadoLikeFavorito();
    }

    private void alternarFavorito() {
        if (usuarioFavoritou) {
            dbHelper.desfavoritarPost(idPost, idUsuarioAtual);
        } else {
            dbHelper.favoritarPost(idPost, idUsuarioAtual);
        }
        atualizarContadores();
        atualizarEstadoLikeFavorito();
    }
}
