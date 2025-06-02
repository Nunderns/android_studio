package com.example.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {

    private EditText editConteudo;
    private Button btnPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        editConteudo = findViewById(R.id.editConteudo);
        btnPublicar = findViewById(R.id.btnPublicar);

        btnPublicar.setOnClickListener(v -> {
            String conteudo = editConteudo.getText().toString().trim();

            if (!conteudo.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("conteudo", conteudo);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Digite algo para publicar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
