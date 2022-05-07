package com.example.sanguage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean enabled = preferences.getBoolean("enabled", false);
                Intent intent;
                if (enabled) {
                    intent = new Intent(MainActivity.this, AppWindowAccount.class);
                } else {
                    intent = new Intent(MainActivity.this, ChooseActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, SPLASH_SCREEN);
    }
}