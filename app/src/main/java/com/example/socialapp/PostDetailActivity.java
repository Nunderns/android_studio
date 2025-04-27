package com.example.socialapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        TextView txtConteudoDetalhe = findViewById(R.id.txtConteudoDetalhe);

        String conteudo = getIntent().getStringExtra("conteudo");
        txtConteudoDetalhe.setText(conteudo);
    }
}
