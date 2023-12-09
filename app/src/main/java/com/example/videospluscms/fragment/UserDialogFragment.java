package com.example.videospluscms.fragment;

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

public class UserDialogFragment extends DialogFragment {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username = null;
    private String password = null;
    private final Activity activity;

    public UserDialogFragment(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_user, null);

        usernameEditText = view.findViewById(R.id.username_editText);
        passwordEditText = view.findViewById(R.id.password_editText);

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
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }


    private void makeUserOperation() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", username);
        jsonBody.put("password", password);
        String requestBody = jsonBody.toString();
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://34.34.73.78:8080/users/register",
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
