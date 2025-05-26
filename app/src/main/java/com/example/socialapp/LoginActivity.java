package com.example.socialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editEmail, editSenha;
    private Button btnLogin, btnCriarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            int userId = obterIdUsuario(email, senha);

            if (userId != -1) {
                // Salva ID do usuário logado
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", userId);
                editor.apply();

                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FeedActivity.class));
            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCriarConta.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    // Busca o ID do usuário logado
    private int obterIdUsuario(String email, String senha) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT id FROM usuarios WHERE email = ? AND senha = ?",
                new String[]{email, senha}
        );

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0); // coluna "id"
        }
        cursor.close();
        return userId;
    }
}
