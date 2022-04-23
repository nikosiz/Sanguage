package com.example.sanguagelogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText username_et;
    private EditText email_et;
    private EditText password_et;
    private MaterialRippleLayout signup_mrl;
    private Button signup_btn;
    private TextView username_tv;
    private TextView email_tv;
    private TextView password_tv;
    private TextView language_tv;
    private RadioGroup language_rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username_et = findViewById(R.id.editTextUsername);
        email_et = findViewById(R.id.editTextEmailAddress);
        password_et = findViewById(R.id.editTextPassword);
        signup_mrl = findViewById(R.id.sign_up_mrl);
        signup_btn=findViewById(R.id.sign_up_btn);
        username_tv = findViewById(R.id.TextViewUsername);
        email_tv = findViewById(R.id.TextViewEmailAddress);
        password_tv = findViewById(R.id.TextViewPassword);
        language_tv = findViewById(R.id.TextViewLanguage);
        language_rg = findViewById(R.id.radio_group);

        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String password = password_et.getText().toString();
                if (!b && !password.isEmpty()) {
                    if (!validatePassword(password))
                        Toast.makeText(getApplicationContext(), "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                }
            }
        });
        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String email = email_et.getText().toString();
                if (!b && !email.isEmpty()) {
                    if (!validateEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Provide correct email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signup_mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = username_et.getText().toString();
                String email = email_et.getText().toString();
                String password = password_et.getText().toString();
                int languageIndex = language_rg.getCheckedRadioButtonId();
                boolean validateAllData = validateAllData(username, email, password, languageIndex);
                if (validateAllData) {
                    disableSignupButton();
                    signUpRequest(username, email, password, mapLanguageIndexToName(languageIndex));
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void disableSignupButton(){
        signup_btn.setEnabled(false);
        signup_btn.setBackgroundColor(R.color.colorAccent);
    }
    @SuppressLint("ResourceAsColor")
    public void enableSignupButton(){
        signup_btn.setEnabled(true);
        signup_btn.setBackgroundColor(R.color.lightPrimary);
    }
    public void signUpRequest(String username, String email, String password, String secondLanguage) {
        String URL = "https://sanguage.herokuapp.com/registration";
        try {
            JSONObject signUpJSON = createSignUpJSON(username, email, password, secondLanguage);
            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, signUpJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //  TODO confirm token window
                    Intent intent = new Intent(getApplicationContext(), MainAppWindow.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("secondLanguage", secondLanguage);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String message = RequestErrorParser.parseError(error);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException j) {
                        Toast.makeText(getApplicationContext(), "unidentified error", Toast.LENGTH_SHORT).show();
                    }
                    enableSignupButton();
                }
            }
            );
            queue.add(jsonObjectRequest);
        } catch (JSONException j) {
            ;
        }
    }

    public JSONObject createSignUpJSON(String username, String email, String password, String secondLanguage) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("secondLanguage", secondLanguage);
        return jsonObject;

    }

    public boolean validateAllData(String username, String email, String password, int languageIndex) {
        if (username.isEmpty()) {
            username_tv.startAnimation(shakeError());
        }
        if (email.isEmpty() || !validateEmail(email)) {
            email_tv.startAnimation(shakeError());
        }
        if (password.isEmpty() || !validatePassword(password)) {
            password_tv.startAnimation(shakeError());
        }
        if (languageIndex == -1) {
            language_tv.startAnimation(shakeError());
        }
        return !username.isEmpty() && validateEmail(email) && validatePassword(password);
    }

    public boolean validatePassword(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public boolean validateEmail(String email) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(5, 15, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    @SuppressLint("NonConstantResourceId")
    public String mapLanguageIndexToName(int index) {
        switch (index) {
            case R.id.learn_english:
                return "English";
            case R.id.learn_french:
                return "French";
            case R.id.learn_german:
                return "German";
            case R.id.learn_spanish:
                return "Spanish";
        }
        return null;
    }
}