package com.example.videospluscms.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.example.videospluscms.R;
import com.example.videospluscms.object.VolleyMultipartRequest;
import com.example.videospluscms.object.VolleySingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MovieVersionsDialogFragment extends DialogFragment {
    private EditText movieIdEditText;
    private Intent myFileIntent;
    private Integer movieId;
    private final Activity activity;
    String filePath;
    String filename;


    public MovieVersionsDialogFragment(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_movie_version, null);

        movieIdEditText = view.findViewById(R.id.movieId_editText);
        Button uploadButton = view.findViewById(R.id.upload_button);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri uri = data.getData();
                        assert uri != null;
                        filePath = getRealPathFromURI(activity, uri);
                    } else {
                        Toast.makeText(activity,"Have to pick a video or operation will fail", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadButton.setOnClickListener(v -> {
            myFileIntent = new Intent(Intent.ACTION_PICK);
            myFileIntent.setType("video/*");
            myFileIntent = Intent.createChooser(myFileIntent, "Choose a video");
            activityResultLauncher.launch(myFileIntent);
        });

        builder.setView(view).setTitle("Movie Version Information").setNegativeButton("Cancel", (dialog, which) -> {
        }).setPositiveButton("Ok", (dialog, which) -> {
            if (movieIdEditText.getText().length() == 0 || filePath == null) {
                Toast.makeText(activity, "Operation failed: Empty id or no video selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Upload started", Toast.LENGTH_SHORT).show();
                movieId = Integer.parseInt(movieIdEditText.getText().toString());
                getFilename();
                uploadMovieVersion();
            }
        });

        return builder.create();
    }

    public void uploadMovieVersion() {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://34.34.73.78:8080/movieVersions/upload",
                response -> Toast.makeText(activity, "Upload Completed", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(activity, "upload failed", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("movieId", movieId.toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData(){
                Map<String, DataPart> params = new HashMap<>();
                try {
                    params.put("upload", new DataPart(filename, Files.readAllBytes(Paths.get(filePath)), "multipart/form-data"));
                } catch (IOException e) {
                    Toast.makeText(activity, "failed upload", Toast.LENGTH_SHORT).show();
                }
                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                999999999,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = VolleySingleton.getInstance(activity).getRequestQueue();
        requestQueue.add(multipartRequest);
    }

    public void getFilename() {
        int lastIndexOf = filePath.lastIndexOf("/");
        filename = filePath.substring(lastIndexOf).substring(1);
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}


