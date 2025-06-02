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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.content.SharedPreferences;
import com.example.socialapp.Post;
import com.example.socialapp.PostAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private void showPopupMenu(View view) {
    PopupMenu popupMenu = new PopupMenu(this, view);
    popupMenu.getMenuInflater().inflate(R.menu.topbar_menu, popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(this, "Saindo da conta...", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    });
    popupMenu.show();
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ImageView menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(v -> showPopupMenu(v));

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

                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                int idUsuario = prefs.getInt("user_id", -1);

                if (idUsuario == -1) {
                    Toast.makeText(this, "Erro: usuário não está logado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String imagem = null;
                String dataAtual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Toast.makeText(this, "Saindo da conta...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
