package com.example.socialapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText editTextSearch;
    private ImageButton buttonSearch;
    private RecyclerView recyclerSearchResults;
    private DatabaseHelper dbHelper;
    private PostAdapter adapter;
    private List<Post> searchResultsList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Inicializa o dbHelper
        dbHelper = new DatabaseHelper(getContext());

        // Encontra as views do layout
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerSearchResults = view.findViewById(R.id.recyclerSearchResults);

        // Configura o RecyclerView
        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsList = new ArrayList<>(); // Inicializa a lista de resultados
        adapter = new PostAdapter(getContext(), searchResultsList); // Use getContext()
        recyclerSearchResults.setAdapter(adapter);

        // Listener para o botão de busca
        buttonSearch.setOnClickListener(v -> performSearch());

        // Listener para o "Enter" no teclado do EditText
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        return view;
    }

    private void performSearch() {
        String query = editTextSearch.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Digite algo para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Exemplo: Buscar posts pelo nome do autor
        // Você pode expandir essa lógica para buscar em outros campos ou em mais tabelas
        searchResultsList.clear(); // Limpa resultados anteriores
        List<Post> postsFound = dbHelper.buscarPostsPorAutor(query); // Use o método do DatabaseHelper
        searchResultsList.addAll(postsFound);

        adapter.notifyDataSetChanged(); // Notifica o adapter que os dados mudaram

        if (searchResultsList.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum resultado encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    // Se precisar buscar usuários, você pode adicionar um método similar:
    /*
    private void searchUsers(String query) {
        // Implementar lógica de busca de usuários aqui
        // ... chamar dbHelper.buscarUsuarioPorNome(query) por exemplo ...
        // ... atualizar um adapter diferente ou uma lista combinada ...
    }
    */
}
