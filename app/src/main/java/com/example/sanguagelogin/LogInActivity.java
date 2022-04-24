package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;


public class LogInActivity extends AppCompatActivity {

    private Button log_in_btn;
    private EditText username_email_et;
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        log_in_btn = findViewById(R.id.log_in_btn);
        username_email_et = findViewById(R.id.username_email_et);
        password_et = findViewById(R.id.password_et);

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "https://sanguage.herokuapp.com/login";
                String username_email = username_email_et.getText().toString();
                String password = password_et.getText().toString();
                if (username_email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please provide correctly data", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject loginJSON = createLoginJSON(username_email.trim(), password);
                        RequestQueue queue = Volley.newRequestQueue(LogInActivity.this);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, loginJSON, new Response.Listener<JSONObject>() {
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
                                    Log.e("LogInActivity - intent.putExtra()", j.getMessage());
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
                                    Log.e("LogInActivity - onErrorResponse()", j.getMessage());
                                }
                            }
                        });
                        queue.add(jsonObjectRequest);
                    } catch (JSONException j) {
                        Log.e("LogInActivity - create account btn", j.getMessage());
                    }
                }
            }
        });
    }

    public JSONObject createLoginJSON(String username_email, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usernameOrEmail", username_email);
        jsonObject.put("password", password);
        return jsonObject;
    }
}