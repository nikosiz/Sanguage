package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

public class MainActivity extends AppCompatActivity {

    MaterialRippleLayout sign_up_mrl;
    MaterialRippleLayout sign_in_mrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_up_mrl = findViewById(R.id.sign_up_mrl);

        sign_up_mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        sign_in_mrl = findViewById(R.id.sign_in_mrl);

        sign_in_mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}