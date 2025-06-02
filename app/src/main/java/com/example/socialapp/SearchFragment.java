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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        dbHelper = new DatabaseHelper(getContext());

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerSearchResults = view.findViewById(R.id.recyclerSearchResults);

        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsList = new ArrayList<>();
        adapter = new PostAdapter(getContext(), searchResultsList);
        recyclerSearchResults.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> performSearch());

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

        searchResultsList.clear();
        List<Post> postsFound = dbHelper.buscarPostsPorAutor(query);
        searchResultsList.addAll(postsFound);

        adapter.notifyDataSetChanged();

        if (searchResultsList.isEmpty()) {
            Toast.makeText(getContext(), "Nenhum resultado encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}
