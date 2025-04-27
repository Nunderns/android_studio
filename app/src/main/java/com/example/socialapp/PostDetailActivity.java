package com.example.socialapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    private TextView txtAutorDetail, txtConteudoDetail, txtComentariosDetail, txtCurtidasDetail, txtFavoritosDetail;
    private ImageView imgPostDetail;

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

        // Recebendo os dados que foram enviados pelo Intent
        String autor = getIntent().getStringExtra("autor");
        String conteudo = getIntent().getStringExtra("conteudo");
        int imagemResId = getIntent().getIntExtra("imagemResId", 0);
        int comentarios = getIntent().getIntExtra("comentarios", 0);
        int curtidas = getIntent().getIntExtra("curtidas", 0);
        int favoritos = getIntent().getIntExtra("favoritos", 0);

        txtAutorDetail.setText(autor);
        txtConteudoDetail.setText(conteudo);
        txtComentariosDetail.setText(String.valueOf(comentarios));
        txtCurtidasDetail.setText(String.valueOf(curtidas));
        txtFavoritosDetail.setText(String.valueOf(favoritos));

        if (imagemResId != 0) {
            imgPostDetail.setImageResource(imagemResId);
            imgPostDetail.setVisibility(ImageView.VISIBLE);
        } else {
            imgPostDetail.setVisibility(ImageView.GONE);
        }
    }
}
