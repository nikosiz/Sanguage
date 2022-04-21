package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainAppWindow extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    ChipNavigationBar navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_window);

        loadFragment(new LearnFragment());

        navBar = findViewById(R.id.bottomNav);
        navBar.setOnItemSelectedListener(this);
    }

    private void loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        } else {
            Toast.makeText(this, "Fragment error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu, menu);
        return true;
    }

    public void onItemSelected(int i) {

        Fragment fragment = null;

        switch (i) {
            case R.id.learn_tab:
                fragment = new LearnFragment();
                break;
            case R.id.your_database_tab:
                fragment = new YourDatabaseFragment();
                break;
            case R.id.add_new_tab:
                fragment = new AddNewFragment();
                break;
            case R.id.search_tab:
                fragment = new SearchFragment();
                break;
            case R.id.your_profile_tab:
                fragment = new YourProfileFragment();
                break;
        }
        loadFragment(fragment);

    }
}