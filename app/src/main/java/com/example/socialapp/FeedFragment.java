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

    private static final int REQUEST_CODE_NOVO_POST = 1;

    private RecyclerView recyclerPosts;
    private FloatingActionButton btnNovoPost;
    private List<Post> postList;
    private PostAdapter adapter;
    private DatabaseHelper dbHelper;

    public FeedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        dbHelper = new DatabaseHelper(getContext());

        recyclerPosts = view.findViewById(R.id.recyclerPosts);
        btnNovoPost = view.findViewById(R.id.btnNovoPost);
        ImageView menuIcon = view.findViewById(R.id.menuIcon);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        menuIcon.setOnClickListener(v -> showPopupMenu(v));

        btnNovoPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NOVO_POST);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carregarPostsDoBanco();
    }

    private void carregarPostsDoBanco() {
        try {
            postList = dbHelper.buscarTodosPosts();
            if (adapter == null) {
                adapter = new PostAdapter(getContext(), postList);
                recyclerPosts.setAdapter(adapter);
            } else {
                adapter.updatePostsList(postList);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao carregar posts!", Toast.LENGTH_SHORT).show();

            postList = new ArrayList<>();
            adapter = new PostAdapter(getContext(), postList);
            recyclerPosts.setAdapter(adapter);
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view); 
        popupMenu.getMenuInflater().inflate(R.menu.topbar_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(getContext(), "Saindo da conta...", Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
                prefs.edit().clear().apply();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NOVO_POST && resultCode == RESULT_OK && data != null) {
            String conteudo = data.getStringExtra("conteudo");
            String imagem = data.getStringExtra("imagem"); // Get the image path if it exists
            
            if (conteudo != null && !conteudo.isEmpty()) {
                SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
                int idUsuario = prefs.getInt("user_id", -1);

                if (idUsuario == -1) {
                    Toast.makeText(getContext(), "Erro: usuário não está logado.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String dataAtual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); 

                dbHelper.inserirPost(idUsuario, conteudo, imagem, dataAtual);
                carregarPostsDoBanco();
            }
        }
    }
}
