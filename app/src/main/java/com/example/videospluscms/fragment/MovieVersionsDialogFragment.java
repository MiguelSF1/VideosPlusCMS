package com.example.videospluscms.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.videospluscms.R;
import com.example.videospluscms.object.UploadThread;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;

public class MovieVersionsDialogFragment extends DialogFragment {
    private EditText movieIdEditText;
    private Intent myFileIntent;
    private Integer movieId;
    private final Activity activity;
    String filePath;

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


        uploadButton.setOnClickListener(v -> {
            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("video/*");
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    myFileIntent.setType("video/*");
                    startActivityForResult(galleryIntent, 10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        builder.setView(view).setTitle("Movie Version Information").setNegativeButton("Cancel", (dialog, which) -> {
        }).setPositiveButton("Ok", (dialog, which) -> {
            if (movieIdEditText.getText().length() == 0 || filePath == null) {
                Toast.makeText(activity, "Operation failed: Empty id or no video selected", Toast.LENGTH_SHORT).show();
            } else {
                movieId = Integer.parseInt(movieIdEditText.getText().toString());
                UploadThread uploadThread = new UploadThread(movieId, filePath, requireActivity().getApplicationContext());
                uploadThread.start();
            }
        });

        return builder.create();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Context context = requireContext().getApplicationContext();
            assert uri != null;
            videoSend(context, uri);
        } else {
            Toast.makeText(requireContext(),"Have to pick a video or operation will fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void videoSend(@NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return;
        filePath = context.getApplicationInfo().dataDir + File.separator + getFileName(uri);
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return;
            OutputStream outputStream = Files.newOutputStream(file.toPath());
            byte[] buf = new byte[20971520];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
        }
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            myFileIntent.setType("video/");
            startActivityForResult(galleryIntent, 10);
        } else {
            Toast.makeText(getContext(), "Unable to access files.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
