package com.example.videospluscms.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.videospluscms.R;
import com.example.videospluscms.object.User;
import com.example.videospluscms.object.VolleySingleton;

import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    List<User> users;

    public UserListAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_user, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());

        holder.deleteButton.setOnClickListener(v-> {
            RequestQueue requestQueue = VolleySingleton.getInstance(holder.itemView.getContext()).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://34.34.73.78:8080/users/" + users.get(position).getUserId(),
                    response -> Toast.makeText(holder.itemView.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(holder.itemView.getContext(), "User deletion failed", Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}