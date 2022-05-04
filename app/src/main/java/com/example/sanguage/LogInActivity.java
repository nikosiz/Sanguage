package com.example.sanguage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.utils.RequestErrorParser;
import com.example.sanguage.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LogInActivity extends AppCompatActivity {

    private Button log_in_btn;
    private EditText username_email_et;
    private EditText password_et;
    private TextView log_in_forgot_password_btn;
    private TextView log_in_sign_up_btn;
    private RelativeLayout log_in_progress_bar;
    private TranslateAnimation shakeError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        log_in_btn = findViewById(R.id.log_in_log_in_btn);
        username_email_et = findViewById(R.id.log_in_username_or_email_et);
        password_et = findViewById(R.id.log_in_password_et);
        log_in_forgot_password_btn = findViewById(R.id.log_in_forgot_password_btn);
        log_in_sign_up_btn = findViewById(R.id.log_in_sign_up_btn);
        log_in_progress_bar = findViewById(R.id.log_in_progress_bar);
        shakeError = Utils.shakeError(5, 15, 0, 0, 500, 7);

        handleLoginBtn();

        log_in_forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        log_in_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    public void handleLoginBtn() {
        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_email = username_email_et.getText().toString();
                String password = password_et.getText().toString();
                if (username_email.isEmpty() || password.isEmpty()) {
                    password_et.startAnimation(shakeError);
                    username_email_et.startAnimation(shakeError);
                } else {
                    showProgressBar();
                    disableAllActions();
                    logInRequest(username_email, password);
                }
            }
        });
    }

    public void disableAllActions() {
        log_in_btn.setEnabled(false);
        password_et.setEnabled(false);
        username_email_et.setEnabled(false);
    }

    public void enableAllActions() {
        log_in_btn.setEnabled(true);
        password_et.setEnabled(true);
        username_email_et.setEnabled(true);
    }

    public void showProgressBar() {
        log_in_progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        log_in_progress_bar.setVisibility(View.GONE);

    }


    public void logInRequest(String username_email, String password) {
        String URL = "https://sanguage.herokuapp.com/login?usernameEmail=" + username_email + "&password=" + password;
        RequestQueue queue = Volley.newRequestQueue(LogInActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent(getApplicationContext(), MainAppWindow.class);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    editor.putLong("userID", response.getLong("userID"));
                    editor.putBoolean("enabled", true);
                    editor.putString("username", response.getString("username"));
                    editor.putString("email", response.getString("email"));
                    editor.putString("registrationDate", response.getString("registrationDate"));
                    JSONArray secondLanguage = response.getJSONArray("secondLanguage");
                    for (int i = 0; i < secondLanguage.length(); i++) {
                        editor.putString("secondLanguage" + (i + 1), secondLanguage.getString(i).toString());
                    }
                    editor.apply();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = RequestErrorParser.parseError(error);
                    if (message.equalsIgnoreCase("incorrect password")) {
                        password_et.startAnimation(shakeError);
                    } else if (message.equalsIgnoreCase("user not found")) {
                        password_et.startAnimation(shakeError);
                        username_email_et.startAnimation(shakeError);
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException j) {
                    Log.e("SignInActivity - onErrorResponse()", j.getMessage());
                }
                hideProgressBar();
                enableAllActions();
            }
        });
        queue.add(jsonObjectRequest);
    }
}