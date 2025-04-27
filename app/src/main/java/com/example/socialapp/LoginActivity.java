package com.example.socialapp;

import android.content.Intent;
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

        // Inicializa o banco de dados
        dbHelper = new DatabaseHelper(this);

        // Liga as views
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        // Ação do botão de login
        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String senha = editSenha.getText().toString().trim();

            if (loginValido(email, senha)) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FeedActivity.class));
            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do botão de criar conta
        btnCriarConta.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    // Método para validar login no banco
    private boolean loginValido(String email, String senha) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM usuarios WHERE email = ? AND senha = ?",
                new String[]{email, senha}
        );

        boolean loginOk = (cursor.getCount() > 0);
        cursor.close();

        return loginOk;
    }
}
