package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText username_et;
    private EditText email_et;
    private EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username_et = findViewById(R.id.editTextUsername);
        email_et = findViewById(R.id.editTextEmailAddress);
        password_et = findViewById(R.id.editTextPassword);

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
}