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

public class PostDetailActivity extends AppCompatActivity {

    private TextView txtAutorDetail, txtConteudoDetail, txtComentariosDetail, txtCurtidasDetail, txtFavoritosDetail;
    private ImageView imgPostDetail, btnMaisOpcoes;
    private int idPost;
    private int idUsuarioPost;
    private int idUsuarioAtual = 1; // Supondo usuário logado = id 1 (pode ser variável depois)

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
}
