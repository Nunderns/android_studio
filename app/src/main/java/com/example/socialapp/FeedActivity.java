package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOVO_POST = 1;

    private RecyclerView recyclerPosts;
    private FloatingActionButton btnNovoPost;
    private List<Post> postList;
    private PostAdapter adapter;
    private DatabaseHelper dbHelper; // Instância do Banco de Dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        dbHelper = new DatabaseHelper(this); // Inicializar Banco de Dados

        recyclerPosts = findViewById(R.id.recyclerPosts);
        btnNovoPost = findViewById(R.id.btnNovoPost);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));

        carregarPostsDoBanco(); // Carregar posts do SQLite

        btnNovoPost.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NOVO_POST);
        });
    }
    private void carregarPostsDoBanco() {
        try {
            postList = dbHelper.buscarTodosPosts(); // Buscar todos os posts do banco
            adapter = new PostAdapter(this, postList);
            recyclerPosts.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar posts!", Toast.LENGTH_SHORT).show();

            // Garante que o RecyclerView ainda funcione vazio
            postList = new ArrayList<>();
            adapter = new PostAdapter(this, postList);
            recyclerPosts.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NOVO_POST && resultCode == RESULT_OK && data != null) {
            String conteudo = data.getStringExtra("conteudo");

            if (conteudo != null && !conteudo.isEmpty()) {
                // Inserir o novo post no banco
                int idUsuario = 1; // Simulando usuário ID 1
                String imagem = null; // Você pode usar uma imagem padrão depois
                String dataAtual = "2025-04-27"; // Você pode melhorar para gerar a data atual real

                dbHelper.inserirPost(idUsuario, conteudo, imagem, dataAtual);

                // Atualizar a lista de posts na tela
                carregarPostsDoBanco();
            }
        }
    }
}
