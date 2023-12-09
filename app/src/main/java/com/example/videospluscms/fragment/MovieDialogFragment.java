package com.example.videospluscms.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.videospluscms.R;
import com.example.videospluscms.object.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MovieDialogFragment extends DialogFragment {
    private EditText titleEditText, releaseDateEditText, durationEditText, posterEditText, ratingEditText, summaryEditText;
    private String title, poster, summary;
    private final int requestType;
    private int movieId, releaseDate;
    private Integer duration;
    private Float rating;
    private final Activity activity;

    public MovieDialogFragment(Activity activity) {
        requestType = Request.Method.PUT;
        this.activity = activity;
    }

    public MovieDialogFragment(int movieId, String title, int duration, String poster, Float rating, int releaseDate, String summary, Activity activity) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.poster = poster;
        this.rating = rating;
        this.summary = summary;
        this.activity = activity;
        requestType = Request.Method.POST;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_movie, null);

        titleEditText = view.findViewById(R.id.title_editText);
        releaseDateEditText = view.findViewById(R.id.releaseDate_editText);
        durationEditText = view.findViewById(R.id.duration_editText);
        posterEditText = view.findViewById(R.id.poster_editText);
        ratingEditText = view.findViewById(R.id.rating_editText);
        summaryEditText = view.findViewById(R.id.summary_editText);

        if (requestType == Request.Method.POST) {
            titleEditText.setText(title);
            releaseDateEditText.setText(String.valueOf(releaseDate));
            durationEditText.setText(duration.toString());
            posterEditText.setText(poster);
            ratingEditText.setText(rating.toString());
            summaryEditText.setText(summary);
        }

        builder.setView(view).setTitle("Movie Information").setNegativeButton("Cancel", (dialog, which) -> {
        }).setPositiveButton("Ok", (dialog, which) -> {
            if (titleEditText.getText().length() == 0 || releaseDateEditText.getText().length() == 0
                    || durationEditText.getText().length() == 0 || posterEditText.getText().length() == 0
                    || ratingEditText.getText().length() == 0 || summaryEditText.getText().length() == 0) {
                Toast.makeText(activity, "Operation failed: Empty input", Toast.LENGTH_SHORT).show();
            } else {
                title = titleEditText.getText().toString();
                releaseDate = Integer.parseInt(releaseDateEditText.getText().toString());
                duration = Integer.parseInt(durationEditText.getText().toString());
                poster = posterEditText.getText().toString();
                rating = Float.parseFloat(ratingEditText.getText().toString());
                summary = summaryEditText.getText().toString();

                try {
                    makeMovieOperation();

                } catch (JSONException e) {
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }


    private void makeMovieOperation() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        if (requestType == Request.Method.POST) {
            jsonBody.put("id", movieId);
        }
        jsonBody.put("title", title);
        jsonBody.put("releaseDate", releaseDate);
        jsonBody.put("duration", duration);
        jsonBody.put("poster", poster);
        jsonBody.put("rating", rating);
        jsonBody.put("summary", summary);
        String requestBody = jsonBody.toString();
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(requestType, "http://34.34.73.78:8080/movies",
                response -> Toast.makeText(activity, "Completed", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        requestQueue.add(stringRequest);
    }
}