package com.example.socialapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtAutor, txtConteudo;
        ImageView imgPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            txtConteudo = itemView.findViewById(R.id.txtConteudo);
            imgPost = itemView.findViewById(R.id.imgPost);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.txtAutor.setText(post.autor);
        holder.txtConteudo.setText(post.conteudo);
        holder.imgPost.setImageResource(post.imagemResId);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
