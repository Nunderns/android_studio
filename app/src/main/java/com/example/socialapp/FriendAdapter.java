package com.example.socialapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<User> friendList;
    private Context context;
    // Opcional: Adicionar um listener para cliques nos itens
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User friend);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FriendAdapter(Context context, List<User> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User currentFriend = friendList.get(position);

        // Define o nome do amigo
        holder.txtFriendName.setText(currentFriend.getNome());

        // Define a foto de perfil (se a User class tiver a URL ou resource ID)
        // Exemplo: Se fotoPerfil for um caminho de arquivo ou URL, você precisaria de uma biblioteca como Glide ou Picasso
        // Se for um resource ID (int), use:
        // holder.imgFriendAvatar.setImageResource(currentFriend.getFotoPerfil());
        // Por enquanto, vamos usar um ícone padrão
        holder.imgFriendAvatar.setImageResource(R.drawable.ic_user_avatar); // Use seu ícone padrão aqui

        // Configura o listener de clique (opcional)
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> {
                listener.onItemClick(currentFriend);
            });
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFriendAvatar;
        TextView txtFriendName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFriendAvatar = itemView.findViewById(R.id.imgFriendAvatar);
            txtFriendName = itemView.findViewById(R.id.txtFriendName);
        }
    }

    // Método para atualizar a lista de amigos no adapter
    public void updateFriendList(List<User> newList) {
        friendList.clear();
        friendList.addAll(newList);
        notifyDataSetChanged();
    }
}
