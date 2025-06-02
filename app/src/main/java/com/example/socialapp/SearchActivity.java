package com.example.socialapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerTrends);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarUsuarioOuPost(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Opcional: buscar enquanto digita
                return false;
            }
        });
    }

    private void buscarUsuarioOuPost(String query) {
        // 1. Buscar usuário pelo nome
        Cursor cursor = dbHelper.buscarUsuarioPorNome(query);
        if (cursor.moveToFirst()) {
            // Usuário encontrado, abrir perfil (exemplo: abrir uma nova Activity)
            int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            Intent intent = new Intent(this, PerfilActivity.class);
            intent.putExtra("idUsuario", idUsuario);
            startActivity(intent);
            cursor.close();
            return;
        }
        cursor.close();

        List<Post> posts = dbHelper.buscarPostsPorAutor(query);
        if (!posts.isEmpty()) {
            postAdapter = new PostAdapter(this, posts);
            recyclerView.setAdapter(postAdapter);
        } else {
            postAdapter = new PostAdapter(this, posts);
            recyclerView.setAdapter(postAdapter);
        }
    }
}
