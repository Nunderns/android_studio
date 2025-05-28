package com.example.socialapp;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerFriends;
    private FriendAdapter adapter;
    private List<User> friendList;
    private DatabaseHelper dbHelper;
    private int currentUserId = -1; // Para armazenar o ID do usuário logado

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // Inicializa o dbHelper
        dbHelper = new DatabaseHelper(getContext());

        // Recupera o ID do usuário logado
        SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        // Encontra o RecyclerView
        recyclerFriends = view.findViewById(R.id.recyclerFriends);

        // Configura o RecyclerView
        recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        friendList = new ArrayList<>(); // Inicializa a lista de amigos
        adapter = new FriendAdapter(getContext(), friendList); // Use getContext()
        recyclerFriends.setAdapter(adapter);

        // Opcional: Configurar listener de clique no adapter
        adapter.setOnItemClickListener(friend -> {
            // Implemente o que acontecerá ao clicar em um amigo (ex: ir para o perfil)
            Toast.makeText(getContext(), "Clicou em: " + friend.getNome(), Toast.LENGTH_SHORT).show();
            // Exemplo: Iniciar uma Activity de Perfil
            // Intent intent = new Intent(getContext(), ProfileActivity.class);
            // intent.putExtra("user_id", friend.getId());
            // startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Carregar a lista de amigos após a view ser criada
        loadFriends();
    }

    private void loadFriends() {
        if (currentUserId != -1) {
            List<User> friends = dbHelper.buscarAmigosDoUsuario(currentUserId);
            adapter.updateFriendList(friends); // Atualiza a lista no adapter
            if (friends.isEmpty()) {
                Toast.makeText(getContext(), "Você ainda não tem amigos.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não logado.", Toast.LENGTH_SHORT).show();
        }
    }

    // Se precisar de recarregar a lista de amigos (ex: após aceitar um pedido de amizade),
    // você pode chamar loadFriends() novamente em onResume() ou em outro método apropriado.
}
