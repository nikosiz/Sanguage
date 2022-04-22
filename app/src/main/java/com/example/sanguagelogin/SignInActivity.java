package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private EditText username_email_et;
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        log_in_mrl = findViewById(R.id.log_in_mrl);
        username_email_et = findViewById(R.id.username_email_et);
        password_et = findViewById(R.id.password_et);

        log_in_mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "https://sanguage.herokuapp.com/login";
                String username_email = username_email_et.getText().toString();
                String password = password_et.getText().toString();
                if (username_email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please provide correctly data", Toast.LENGTH_SHORT).show();
                }
                else{
                try {
                    JSONObject loginJSON = createLoginJSON(username_email.trim(), password);
                    RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, loginJSON, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Intent intent = new Intent(getApplicationContext(), MainAppWindow.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String message = RequestErrorParser.parseError(error);
                                Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();
                            }catch (JSONException j){
                                Toast.makeText(getApplicationContext(),"unidentified error" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    queue.add(jsonObjectRequest);
                } catch (JSONException jsonException) {
                    Toast.makeText(getApplicationContext(), "please provide correctly data", Toast.LENGTH_SHORT).show();
                }
            }}
        });
    }

    public JSONObject createLoginJSON(String username_email, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usernameOrEmail", username_email);
        jsonObject.put("password", password);
        return jsonObject;
    }
}