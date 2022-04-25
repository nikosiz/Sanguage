package com.example.sanguagelogin;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends AppCompatActivity {

    private MaterialRippleLayout log_in_mrl;
    private Button log_in_btn;
    private EditText username_email_et;
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        log_in_mrl = findViewById(R.id.log_in_mrl);
        log_in_btn = findViewById(R.id.log_in_btn);
        username_email_et = findViewById(R.id.username_email_et);
        password_et = findViewById(R.id.password_et);

        log_in_mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_email = username_email_et.getText().toString();
                String password = password_et.getText().toString();
                if (username_email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please provide correct data", Toast.LENGTH_SHORT).show();
                } else {
                    disableSignInButton();
                    signInRequest(username_email, password);
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void disableSignInButton() {
        log_in_btn.setEnabled(false);
        log_in_btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @SuppressLint("ResourceAsColor")
    public void enableSignInButton() {
        log_in_btn.setEnabled(true);
        log_in_btn.setBackgroundColor(getResources().getColor(R.color.buttons));
    }

    public void signInRequest(String username_email, String password) {
        String URL = "https://sanguage.herokuapp.com/login?usernameEmail=" + username_email + "&password=" + password;
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(getApplicationContext(), MainAppWindow.class);
                try {
                    intent.putExtra("userID", response.getLong("userID"));
                    intent.putExtra("username", response.getString("username"));
                    intent.putExtra("email", response.getString("email"));
                    intent.putExtra("registrationDate", response.getString("registrationDate"));
                    intent.putExtra("userID", response.getLong("userID"));
                } catch (JSONException j) {
                    Log.e("SignInActivity - intent.putExtra()", j.getMessage());
                }
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = RequestErrorParser.parseError(error);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException j) {
                    Log.e("SignInActivity - onErrorResponse()", j.getMessage());
                }
                enableSignInButton();
            }
        });
        queue.add(jsonObjectRequest);
    }
}