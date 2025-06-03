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
    private int currentUserId = -1;

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences prefs = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        recyclerFriends = view.findViewById(R.id.recyclerFriends);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        friendList = new ArrayList<>();
        adapter = new FriendAdapter(getContext(), friendList);
        recyclerFriends.setAdapter(adapter);
        adapter.setOnItemClickListener(friend -> {

            Toast.makeText(getContext(), "Clicou em: " + friend.getNome(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
}
