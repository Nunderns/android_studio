package com.example.socialapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this); // Instancia o DatabaseHelper

        EditText editNome = findViewById(R.id.editNome);
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editSenha);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {
            String nome = editNome.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String senha = editSenha.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {
                // Grava no banco de dados
                ContentValues values = new ContentValues();
                values.put("nome", nome);
                values.put("email", email);
                values.put("senha", senha);
                values.put("bio", ""); // Opcional, vazio por enquanto
                values.put("fotoPerfil", ""); // Opcional, vazio por enquanto

                long id = dbHelper.getWritableDatabase().insert("usuarios", null, values);

                if (id != -1) {
                    Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao registrar usuário!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
