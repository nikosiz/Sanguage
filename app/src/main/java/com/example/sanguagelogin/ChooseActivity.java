package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.button.MaterialButton;

public class ChooseActivity extends AppCompatActivity {

    MaterialRippleLayout choose_sign_up_btn;
    MaterialRippleLayout choose_sign_in_btn;
    MaterialRippleLayout choose_skip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choose_activity);

        choose_sign_up_btn = findViewById(R.id.choose_sign_up_mrl);

        choose_sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        choose_sign_in_btn = findViewById(R.id.choose_sign_in_mrl);

        choose_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });

        choose_skip_btn = findViewById(R.id.choose_skip_mrl);

        choose_skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainAppWindow.class);
                startActivity(intent);
            }
        });


    }
}