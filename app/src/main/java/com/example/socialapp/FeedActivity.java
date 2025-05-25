package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOVO_POST = 1;

    private RecyclerView recyclerPosts;
    private FloatingActionButton btnNovoPost;
    private List<Post> postList;
    private PostAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        dbHelper = new DatabaseHelper(this);

        recyclerPosts = findViewById(R.id.recyclerPosts);
        btnNovoPost = findViewById(R.id.btnNovoPost);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));

        carregarPostsDoBanco();

        btnNovoPost.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NOVO_POST);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void carregarPostsDoBanco() {
        try {
            postList = dbHelper.buscarTodosPosts();
            adapter = new PostAdapter(this, postList);
            recyclerPosts.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao carregar posts!", Toast.LENGTH_SHORT).show();

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
                int idUsuario = 1;
                String imagem = null;
                String dataAtual = "2025-04-27";

                dbHelper.inserirPost(idUsuario, conteudo, imagem, dataAtual);

                carregarPostsDoBanco();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}
