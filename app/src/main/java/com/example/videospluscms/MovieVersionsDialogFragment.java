package com.example.videospluscms;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MovieVersionsDialogFragment extends DialogFragment {
    private EditText formatEditText, resolutionEditText, linkEditText, movieIdEditText;
    private String format, resolution, link;
    private final int requestType;
    private int versionId;
    private Integer movieId;
    private final Activity activity;

    public MovieVersionsDialogFragment(Activity activity) {
        requestType = Request.Method.PUT;
        this.activity = activity;
    }

    public MovieVersionsDialogFragment(int versionId, int movieId, String format, String resolution, String link, Activity activity) {
        this.movieId = movieId;
        this.format = format;
        this.resolution = resolution;
        this.link = link;
        this.versionId = versionId;
        this.activity = activity;
        requestType = Request.Method.POST;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_movie_version, null);

        formatEditText = view.findViewById(R.id.format_editText);
        resolutionEditText = view.findViewById(R.id.resolution_editText);
        linkEditText = view.findViewById(R.id.link_editText);
        movieIdEditText = view.findViewById(R.id.movieId_editText);

        if (requestType == Request.Method.POST) {
            formatEditText.setText(format);
            resolutionEditText.setText(resolution);
            linkEditText.setText(link);
            movieIdEditText.setText(movieId.toString());
        }

        builder.setView(view).setTitle("Movie Version Information").setNegativeButton("Cancel", (dialog, which) -> {
        }).setPositiveButton("Ok", (dialog, which) -> {
            if (formatEditText.getText().length() == 0 || resolutionEditText.getText().length() == 0
            || linkEditText.getText().length() == 0 || movieIdEditText.getText().length() == 0) {
                Toast.makeText(activity, "Operation failed: Empty input", Toast.LENGTH_SHORT).show();
            } else {
                format = formatEditText.getText().toString();
                resolution = resolutionEditText.getText().toString();
                link = linkEditText.getText().toString();
                movieId = Integer.parseInt(movieIdEditText.getText().toString());
                try {
                    makeMovieVersionOperation();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return builder.create();
    }


    private void makeMovieVersionOperation() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        if (requestType == Request.Method.POST) jsonBody.put("id", versionId);
        jsonBody.put("movieId", movieId);
        jsonBody.put("movieFormat", format);
        jsonBody.put("movieResolution", resolution);
        jsonBody.put("movieLink", link);
        String requestBody = jsonBody.toString();
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(requestType, "http://192.168.1.103:8080/api/movieVersions",
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
