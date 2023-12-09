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
import com.example.videospluscms.activity.MainActivity;
import com.example.videospluscms.fragment.MovieDialogFragment;
import com.example.videospluscms.object.Movie;
import com.example.videospluscms.object.VolleySingleton;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    List<Movie> movies;

    public MovieListAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_movie, parent, false);
        return new MovieListAdapter.ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
        holder.movieId.setText(movies.get(position).getMovieId().toString());
        holder.title.setText(movies.get(position).getTitle());

        holder.menuButton.setOnClickListener(v -> {
            MainActivity activity = (MainActivity) holder.itemView.getContext();
            MovieDialogFragment movieDialogFragment = new MovieDialogFragment(
                    movies.get(position).getMovieId(), movies.get(position).getTitle(),
                    movies.get(position).getDuration(), movies.get(position).getPoster(),
                    movies.get(position).getRating(), movies.get(position).getReleaseDate(),
                    movies.get(position).getSummary(), activity);
            movieDialogFragment.show(activity.getSupportFragmentManager(), "Movie Information");
        });

        holder.deleteButton.setOnClickListener(v-> {
            RequestQueue requestQueue = VolleySingleton.getInstance(holder.itemView.getContext()).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://34.34.73.78:8080/movies/" + movies.get(position).getMovieId(),
                    response -> Toast.makeText(holder.itemView.getContext(), "Movie deleted successfully", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(holder.itemView.getContext(), "Movie deletion failed", Toast.LENGTH_SHORT).show());

            requestQueue.add(stringRequest);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, movieId;
        ImageView menuButton, deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            movieId = itemView.findViewById(R.id.movieId);
            menuButton = itemView.findViewById(R.id.menu_button_movie);
            deleteButton = itemView.findViewById(R.id.delete_button_movie);
        }
    }
}
