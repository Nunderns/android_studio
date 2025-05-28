package com.example.socialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editEmail;
    private EditText editSenha;
    private Button btnLogin;
    private TextView btnCriarConta;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar se o usuário já está logado
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            // Usuário já está logado, vá direto para a tela principal (MainActivity)
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza a tela de login para que o usuário não possa voltar
            return; // Interrompe a execução do restante do onCreate
        }

        // Se não estiver logado, configure a tela de login normalmente
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    int userId = dbHelper.verificarLogin(email, senha);
                    if (userId != -1) {
                        // Login bem-sucedido, salve a sessão e vá para a tela principal
                        SharedPreferences.Editor editor = prefs.edit(); // Use as prefs já obtidas
                        editor.putInt("user_id", userId);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza a tela de login
                    } else {
                        Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Adicione este método ao seu DatabaseHelper se ainda não existir
    // public int verificarLogin(String email, String senha) {
    //     SQLiteDatabase db = this.getReadableDatabase();
    //     Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE email = ? AND senha = ?", new String[]{email, senha});
    //     int userId = -1;
    //     if (cursor.moveToFirst()) {
    //         userId = cursor.getInt(0);
    //     }
    //     cursor.close();
    //     db.close();
    //     return userId;
    // }
}
