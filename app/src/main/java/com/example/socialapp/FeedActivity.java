package com.example.socialapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        RecyclerView recyclerView = findViewById(R.id.recyclerFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Post> posts = new ArrayList<>();
        posts.add(new Post("João", "Olá, este é meu primeiro post!", R.drawable.ic_launcher_foreground));
        posts.add(new Post("Maria", "Adoro esse app!", R.drawable.ic_launcher_foreground));

        PostAdapter adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);
    }
}
