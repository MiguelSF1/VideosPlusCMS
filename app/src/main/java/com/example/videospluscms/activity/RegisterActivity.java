package com.example.videospluscms.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.videospluscms.R;
import com.example.videospluscms.object.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, passwordConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        usernameEditText = findViewById(R.id.username_input_register);
        passwordEditText = findViewById(R.id.password_input_register);
        passwordConfirmEditText = findViewById(R.id.password_input2_editText);
        Button signUpButton = findViewById(R.id.sign_up_button);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        signUpButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = passwordConfirmEditText.getText().toString();

            if (!password.equals(confirmPassword) || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                RequestQueue usersRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", username);
                jsonBody.put("password", password);
                String responseBody = jsonBody.toString();

                StringRequest usersStringRequest = new StringRequest(Request.Method.POST, "http://34.34.73.78:8080/usersCMS/register",
                        response -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)),
                        error -> Toast.makeText(RegisterActivity.this, "Failed Register Attempt", Toast.LENGTH_SHORT).show()) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        return responseBody.getBytes(StandardCharsets.UTF_8);
                    }
                };

                usersRequestQueue.add(usersStringRequest);
            } catch (JSONException e) {
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}