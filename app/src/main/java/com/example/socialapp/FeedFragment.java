package com.example.socialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FeedFragment extends Fragment {

    private static final int REQUEST_CODE_NOVO_POST = 1; // Defina a constante aqui

    private RecyclerView recyclerPosts;
    private FloatingActionButton btnNovoPost;
    private List<Post> postList;
    private PostAdapter adapter;
    private DatabaseHelper dbHelper;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // Inicialize o dbHelper aqui
        dbHelper = new DatabaseHelper(getContext());

        // Encontre as views do layout do fragment
        recyclerPosts = view.findViewById(R.id.recyclerPosts);
        btnNovoPost = view.findViewById(R.id.btnNovoPost);
        ImageView menuIcon = view.findViewById(R.id.menuIcon);

        // Configura o RecyclerView
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adiciona listener para o menu
        menuIcon.setOnClickListener(v -> showPopupMenu(v));

        // Adiciona listener para o botão de novo post
        btnNovoPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NOVO_POST);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Carregar os posts após a view ser criada
        carregarPostsDoBanco();
    }

    private void carregarPostsDoBanco() {
        try {
            postList = dbHelper.buscarTodosPosts();
            adapter = new PostAdapter(getContext(), postList); // Use getContext() ou getActivity()
            recyclerPosts.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao carregar posts!", Toast.LENGTH_SHORT).show();

            postList = new ArrayList<>();
            adapter = new PostAdapter(getContext(), postList);
            recyclerPosts.setAdapter(adapter);
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view); // Use getContext()
        popupMenu.getMenuInflater().inflate(R.menu.topbar_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(getContext(), "Saindo da conta...", Toast.LENGTH_SHORT).show();

                // Limpar a sessão do usuário (SharedPreferences)
                SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
                prefs.edit().clear().apply();

                // Redirecionar para LoginActivity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish(); // Finaliza a MainActivity
                return true;
            }
            return false;
        });
        popupMenu.show();
    }


    // Método para receber o resultado da CreatePostActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NOVO_POST && resultCode == RESULT_OK && data != null) {
            String conteudo = data.getStringExtra("conteudo");

            if (conteudo != null && !conteudo.isEmpty()) {
                // Recupera o ID do usuário logado via SharedPreferences
                SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
                int idUsuario = prefs.getInt("user_id", -1);

                if (idUsuario == -1) {
                    Toast.makeText(getContext(), "Erro: usuário não está logado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String imagem = null; // Implemente a lógica de imagem se necessário
                String dataAtual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); // Adicionado HH:mm:ss para ordenação mais precisa

                dbHelper.inserirPost(idUsuario, conteudo, imagem, dataAtual);
                carregarPostsDoBanco(); // Recarrega a lista para mostrar o novo post
            }
        }
    }

    // Métodos Activity-specific como onWindowFocusChanged e hideSystemUI não são necessários aqui.
    // Menu Options também é tratado pelo PopupMenu.
}
