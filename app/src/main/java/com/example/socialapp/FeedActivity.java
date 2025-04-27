package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recyclerPosts;
    private FloatingActionButton btnNovoPost;
    private List<Post> postList;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerPosts = findViewById(R.id.recyclerPosts);
        btnNovoPost = findViewById(R.id.btnNovoPost);

        // Configurar RecyclerView
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        recyclerPosts.setAdapter(adapter);

        // Clique no botão de novo post
        btnNovoPost.setOnClickListener(v -> {
            // Aqui você pode abrir a tela de Criar Post
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivity(intent);
        });
    }
}
