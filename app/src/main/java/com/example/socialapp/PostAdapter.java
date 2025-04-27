package com.example.socialapp;

import android.content.Context;
import android.content.Intent;
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
    private final Context context; // Corrigido: precisamos do contexto para abrir Activity

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtAutor, txtConteudo;
        ImageView imgPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            txtConteudo = itemView.findViewById(R.id.textConteudo);
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
        holder.txtAutor.setText(post.getAutor());
        holder.txtConteudo.setText(post.getConteudo());
        holder.imgPost.setImageResource(post.getImagemResId());

        // Aqui sim colocamos o click no lugar certo:
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("autor", post.getAutor());
            intent.putExtra("conteudo", post.getConteudo());
            intent.putExtra("imagemResId", post.getImagemResId());
            intent.putExtra("comentarios", post.getComentarios());
            intent.putExtra("curtidas", post.getCurtidas());
            intent.putExtra("favoritos", post.getFavoritos());
            intent.putExtra("idPost", post.getId());
            intent.putExtra("idUsuario", post.getIdUsuario());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
