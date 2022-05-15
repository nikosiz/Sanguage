package com.example.sanguage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioGroup;
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
import com.example.sanguage.utils.VolleyRequestCallback;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText sign_up_username_et;
    private TextInputEditText sign_up_email_et;
    private TextInputEditText sign_up_password_et;
    private Button sign_up_sign_up_btn;
    private TextView sign_up_language_tv;
    private RadioGroup sign_up_language_rg;
    private TextView sign_up_sign_in_btn;
    private HashSet<Integer> languages_buttons_ids;
    private RelativeLayout sign_up_progress_bar;
    private TranslateAnimation shakeError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        sign_up_username_et = findViewById(R.id.sign_up_username_et);
        sign_up_email_et = findViewById(R.id.sign_up_email_et);
        sign_up_password_et = findViewById(R.id.sign_up_password_et);
        sign_up_sign_up_btn = findViewById(R.id.sign_up_sign_up_mtn);
        sign_up_language_tv = findViewById(R.id.sign_up_choose_language_tv);
        sign_up_language_rg = findViewById(R.id.sign_up_language_rg);
        languages_buttons_ids = new HashSet<>();
        languages_buttons_ids.add(R.id.learn_english);
        languages_buttons_ids.add(R.id.learn_french);
        languages_buttons_ids.add(R.id.learn_german);
        languages_buttons_ids.add(R.id.learn_spanish);
        sign_up_sign_in_btn = findViewById(R.id.sign_up_sign_in_mtn);
        sign_up_progress_bar = findViewById(R.id.sign_up_progress_bar);
        shakeError = Utils.shakeError(5, 10, 0, 0, 500, 7);
        passwordFocusChanged();
        emailFocusChanged();
        handleRegisterBtn();
        greyOutLanguages();

        sign_up_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void greyOutLanguages() {
        sign_up_language_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (int button_id : languages_buttons_ids) {
                    MaterialRadioButton materialRadioButton = (MaterialRadioButton) findViewById(button_id);
                    if (i == button_id) {
                        materialRadioButton.setAlpha(1.f);
                    } else {
                        materialRadioButton.setAlpha(.5f);
                    }
                }
            }
        });
    }

    public void handleRegisterBtn() {
        sign_up_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = sign_up_username_et.getText().toString();
                String email = sign_up_email_et.getText().toString();
                String password = sign_up_password_et.getText().toString();
                int languageIndex = sign_up_language_rg.getCheckedRadioButtonId();
                boolean validateAllData = validateAllData(username, email, password, languageIndex);
                if (validateAllData) {
                    disableAllActions();
                    showProgressBar();
                    signUpRequest(username, email, password, mapLanguageIndexToName(languageIndex), new VolleyRequestCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getApplicationContext(), WaitingForConfirmationActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    });
                }
            }
        });
    }

    public void emailFocusChanged() {
        sign_up_email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String email = sign_up_email_et.getText().toString();
                if (!b && !email.isEmpty()) {
                    if (!Utils.validateEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Provide correct email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void passwordFocusChanged() {
        sign_up_password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String password = sign_up_password_et.getText().toString();
                if (!b && !password.isEmpty()) {
                    if (!Utils.validatePassword(password))
                        Toast.makeText(getApplicationContext(), "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void disableAllActions() {
        sign_up_username_et.setEnabled(false);
        sign_up_email_et.setEnabled(false);
        sign_up_password_et.setEnabled(false);
        sign_up_sign_in_btn.setEnabled(false);
        sign_up_sign_up_btn.setEnabled(false);
        for (int button_id : languages_buttons_ids) {
            findViewById(button_id).setEnabled(false);
        }
    }

    public void enableAllActions() {
        sign_up_username_et.setEnabled(true);
        sign_up_email_et.setEnabled(true);
        sign_up_password_et.setEnabled(true);
        sign_up_sign_in_btn.setEnabled(true);
        sign_up_sign_up_btn.setEnabled(true);
        for (int button_id : languages_buttons_ids) {
            findViewById(button_id).setEnabled(true);
        }
    }

    public void showProgressBar() {
        sign_up_progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        sign_up_progress_bar.setVisibility(View.GONE);
    }

    public void signUpRequest(String username, String email, String password, String secondLanguage, final VolleyRequestCallback callback) {
        String URL = "https://sanguage.herokuapp.com/registration";
        try {
            JSONObject signUpJSON = createSignUpJSON(username, email, password, secondLanguage);
            RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, signUpJSON, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String userID = response.getString("messages");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("userID", Long.valueOf(userID));
                        editor.apply();
                        callback.onSuccess();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String message = RequestErrorParser.parseError(error);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    Log.e("signUpRequest - onErrorResponse()", String.valueOf(error));
                    hideProgressBar();
                    enableAllActions();
                }
            }
            );
            queue.add(jsonObjectRequest);
        } catch (JSONException j) {
            Log.e("SignUp - signUpRequest()", j.getMessage());
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
            sign_up_username_et.startAnimation(shakeError);
        }
        if (email.isEmpty() || !Utils.validateEmail(email)) {
            sign_up_email_et.startAnimation(shakeError);
        }
        if (password.isEmpty() || !Utils.validatePassword(password)) {
            sign_up_password_et.startAnimation(shakeError);
        }
        if (languageIndex == -1) {
            sign_up_language_tv.startAnimation(shakeError);
            sign_up_language_rg.startAnimation(shakeError);
            return false;
        }
        return !username.isEmpty() && Utils.validateEmail(email) && Utils.validatePassword(password);
    }


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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}