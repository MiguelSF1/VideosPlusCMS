package com.example.videospluscms;

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

import java.util.List;

public class MovieVersionsListAdapter extends RecyclerView.Adapter<MovieVersionsListAdapter.ViewHolder> {
    List<MovieVersion> movieVersions;

    public MovieVersionsListAdapter(List<MovieVersion> movieVersions) {
        this.movieVersions = movieVersions;
    }

    @NonNull
    @Override
    public MovieVersionsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_movie_version, parent, false);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieVersionsListAdapter.ViewHolder holder, int position) {
        //holder.title.setText(movieVersions.get(position).getMovieId());
        holder.format.setText(movieVersions.get(position).getMovieFormat());
        holder.resolution.setText(movieVersions.get(position).getMovieResolution());

        holder.menuButton.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) holder.itemView.getContext();
            MovieVersionsDialogFragment movieVersionsDialogFragment = new MovieVersionsDialogFragment(movieVersions.get(position).getMovieId(),
                    movieVersions.get(position).getMovieFormat(), movieVersions.get(position).getMovieResolution(), movieVersions.get(position).getMovieLink(), activity);
            movieVersionsDialogFragment.show(activity.getSupportFragmentManager(), "Movie Version Information");
        });

        holder.deleteButton.setOnClickListener(v-> {
            RequestQueue requestQueue = VolleySingleton.getInstance(holder.itemView.getContext()).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://192.168.1.103:8080/api/movieVersions/" + movieVersions.get(position).getMovieId(),
                    response -> Toast.makeText(holder.itemView.getContext(), "Movie Version deleted successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(holder.itemView.getContext(), "Movie Version deletion failed", Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);
        });
    }

    @Override
    public int getItemCount() {
        return movieVersions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, format, resolution;
        ImageView menuButton, deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_movieVersion);
            format = itemView.findViewById(R.id.format_movieVersion);
            resolution = itemView.findViewById(R.id.resolution_movieVersion);
            menuButton = itemView.findViewById(R.id.menu_button_movieVersion);
            deleteButton = itemView.findViewById(R.id.delete_button_movieVersion);
        }
    }
}