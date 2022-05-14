package com.example.sanguage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitingForConfirmationActivity extends AppCompatActivity {

    private Button openEmailApp;
    private RelativeLayout progress_bar;
    private RequestQueue requestQueue;
    private Long userID;
    private boolean enabled;



    public void getUserEnabledRequest() {
        String URL = "https://sanguage.herokuapp.com/user/getUserEnabled?userID=" + userID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("messages");
                    handleResponse(message);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void handleResponse(String response) {
        if (response.equals("enabled")) {
            enabled = true;
        } else {
            enabled = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_confirmation);

        openEmailApp = findViewById(R.id.waiting_open_email_btn);
        requestQueue = Volley.newRequestQueue(WaitingForConfirmationActivity.this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = preferences.getLong("userID", 9999999999999L);
        progress_bar.setVisibility(View.VISIBLE);
        requestInBackground();
        openEmailApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
                finish();
            }
        });
    }

    public void requestInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (enabled) {
                        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                        intent.putExtra("afterSignup",true);
                        startActivity(intent);
                        break;
                    }
                    getUserEnabledRequest();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}