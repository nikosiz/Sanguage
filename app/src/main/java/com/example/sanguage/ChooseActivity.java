package com.example.sanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ChooseActivity extends AppCompatActivity {

    private Button choose_sign_up_btn, choose_sign_in_btn;
    private TextView choose_skip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choose_activity);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChooseActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("enabled", false);
        editor.apply();

        choose_sign_up_btn = findViewById(R.id.choose_sign_up_btn);

        choose_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        choose_sign_in_btn = findViewById(R.id.choose_log_in_btn);

        choose_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });

        choose_skip_btn = findViewById(R.id.choose_skip_btn);

        choose_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppWindowAccount.class);
                startActivity(intent);
            }
        });


    }
}