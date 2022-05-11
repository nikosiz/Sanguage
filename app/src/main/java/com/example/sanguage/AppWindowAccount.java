package com.example.sanguage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AppWindowAccount extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar nav_bar;
    private Fragment currentFragment;
    private Long userID;
    private LearnFragment learnFragment;

    @Override
    public void onBackPressed() {
        ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_window);
        nav_bar = findViewById(R.id.bottom_nav);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = preferences.getLong("userID", 9999999999999L);
        learnFragment=new LearnFragment(userID);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, learnFragment).commit();
        nav_bar.setItemSelected(R.id.bottom_nav_learn, true);
        nav_bar.setOnItemSelectedListener(this);
    }

    private void loadFragment() {
        if (currentFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentFragment).commit();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Toast.makeText(this, "Fragment error", Toast.LENGTH_SHORT).show();
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu_acc, menu);
        return true;
    }

    public void onItemSelected(int i) {
        if (i == R.id.bottom_nav_learn) {
            currentFragment = learnFragment;
        } else if (i == R.id.bottom_nav_database) {
            currentFragment = new DatabaseFragment(userID);
        } else if (i == R.id.bottom_nav_add_new) {
            currentFragment = new AddNewFragment();
        } else if (i == R.id.bottom_nav_search) {
            currentFragment = new SearchFragment();
        } else if (i == R.id.bottom_nav_profile) {
            currentFragment = new ProfileFragment(userID);
        }
        loadFragment();
    }
}