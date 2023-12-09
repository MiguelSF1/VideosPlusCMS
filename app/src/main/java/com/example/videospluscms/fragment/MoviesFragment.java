package com.example.videospluscms.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.videospluscms.adapter.MovieListAdapter;
import com.example.videospluscms.R;
import com.example.videospluscms.object.Movie;
import com.example.videospluscms.object.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment {
    private RecyclerView recyclerView;
    public MoviesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle SavedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(v -> {
            MovieDialogFragment movieDialogFragment = new MovieDialogFragment(getActivity());
            movieDialogFragment.show(requireActivity().getSupportFragmentManager(), "Movie Information");
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMovies();

    }

    private void getMovies() {
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest moviesStringRequest = new StringRequest(Request.Method.GET, "http://34.34.73.78:8080/movies", response -> {
            Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
            List<Movie> movies = new Gson().fromJson(response, listType);
            MovieListAdapter movieListAdapter = new MovieListAdapter(movies);
            recyclerView.setAdapter(movieListAdapter);
        }, error -> Log.d("failure", "sendRequestMovies: Failed "));

        requestQueue.add(moviesStringRequest);
    }
}
