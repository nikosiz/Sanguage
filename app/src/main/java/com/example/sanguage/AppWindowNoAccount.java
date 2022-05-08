package com.example.sanguage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AppWindowNoAccount extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar nav_bar;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_window_no_account);

        nav_bar = findViewById(R.id.bottom_nav_no);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LearnFragment()).commit();
        nav_bar.setItemSelected(R.id.bottom_nav_learn, true);
        nav_bar.setOnItemSelectedListener(this);
    }

    private void loadFragment() {
        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentFragment).commit();
        } else {
            Toast.makeText(this, "Fragment error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu_no_acc, menu);
        return true;
    }

    public void onItemSelected(int i) {
        if (i == R.id.bottom_nav_learn) {
            currentFragment = new LearnFragment();
        } else if (i == R.id.bottom_nav_profile) {
            currentFragment = new CreateAccountFragment();
        }
        loadFragment();
    }
}