package com.example.socialapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView txtNome, txtEmail, txtBio;
    private ImageView imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        dbHelper = new DatabaseHelper(this);
        txtNome = findViewById(R.id.txtNomePerfil);
        txtEmail = findViewById(R.id.txtEmailPerfil);
        txtBio = findViewById(R.id.txtBioPerfil);
        imgPerfil = findViewById(R.id.imgPerfil);

        int idUsuario = getIntent().getIntExtra("idUsuario", -1);
        if (idUsuario != -1) {
            carregarPerfil(idUsuario);
        }
    }

    private void carregarPerfil(int idUsuario) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT nome, email, bio, fotoPerfil FROM usuarios WHERE id = ?",
                new String[]{String.valueOf(idUsuario)}
        );
        if (cursor.moveToFirst()) {
            txtNome.setText(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            txtEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            txtBio.setText(cursor.getString(cursor.getColumnIndexOrThrow("bio")));
        }
        cursor.close();
    }
} 