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

public class UserDialogFragment extends DialogFragment {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username = null;
    private String password = null;
    private final int requestType;
    private int userId = -1;
    private final Activity activity;

    public UserDialogFragment(Activity activity) {
        requestType = Request.Method.PUT;
        this.activity = activity;
    }

    public UserDialogFragment(int userId,String username, String password, Activity activity) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.activity = activity;
        requestType = Request.Method.POST;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_user, null);

        usernameEditText = view.findViewById(R.id.username_editText);
        passwordEditText = view.findViewById(R.id.password_editText);

        if (username != null && password != null) {
            usernameEditText.setText(username);
            passwordEditText.setText(password);
        }

        builder.setView(view).setTitle("User Information").setNegativeButton("Cancel", (dialog, which) -> {
        }).setPositiveButton("Ok", (dialog, which) -> {
            if (usernameEditText.getText().length() == 0 || passwordEditText.getText().length() == 0) {
                Toast.makeText(activity, "Operation failed: Empty input", Toast.LENGTH_SHORT).show();
            } else {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                try {
                    makeUserOperation();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return builder.create();
    }


    private void makeUserOperation() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        if (userId != -1) jsonBody.put("id", userId);
        jsonBody.put("username", username);
        jsonBody.put("password", password);
        String requestBody = jsonBody.toString();
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(requestType, "http://192.168.1.103:8080/api/users",
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
