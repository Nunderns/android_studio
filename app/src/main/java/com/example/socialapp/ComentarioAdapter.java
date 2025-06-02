package com.example.socialapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {
    private final List<Comentario> comentarios;
    private final Context context;
    private final OnResponderClickListener responderClickListener;

    public interface OnResponderClickListener {
        void onResponderClick(Comentario comentario);
    }

    public ComentarioAdapter(Context context, List<Comentario> comentarios, OnResponderClickListener responderClickListener) {
        this.context = context;
        this.comentarios = comentarios;
        this.responderClickListener = responderClickListener;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);
        holder.txtAutor.setText(comentario.getNomeAutor());
        holder.txtTexto.setText(comentario.getTexto());
        holder.txtData.setText(comentario.getData());
        holder.btnResponder.setOnClickListener(v -> responderClickListener.onResponderClick(comentario));
        if (comentario.getIdComentarioPai() != null) {
            holder.itemView.setPadding(64, 0, 0, 0);
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView txtAutor, txtTexto, txtData;
        ImageButton btnResponder;
        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAutor = itemView.findViewById(R.id.txtAutorComentario);
            txtTexto = itemView.findViewById(R.id.txtTextoComentario);
            txtData = itemView.findViewById(R.id.txtDataComentario);
            btnResponder = itemView.findViewById(R.id.btnResponderComentario);
        }
    }
} 