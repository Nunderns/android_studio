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

        holder.txtFriendName.setText(currentFriend.getNome());
        holder.imgFriendAvatar.setImageResource(R.drawable.ic_user_avatar);

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

    public void updateFriendList(List<User> newList) {
        friendList.clear();
        friendList.addAll(newList);
        notifyDataSetChanged();
    }
}
