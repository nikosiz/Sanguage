package com.example.sanguage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sanguage.pojo.DictionaryPojo;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class AppWindowAccount extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar navBar;
    private LearnFragment learnFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_window);

        navBar = findViewById(R.id.bottomNav);
        currentFragment = null;
        ArrayList<DictionaryPojo> objects = new ArrayList<>();
        objects.add(new DictionaryPojo("DASDASD"));
        learnFragment = new LearnFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, learnFragment).commit();
        navBar.setItemSelected(R.id.bottom_nav_learn, true);
        navBar.setOnItemSelectedListener(this);

    }

    private void loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        } else {
            Toast.makeText(this, "Fragment error", Toast.LENGTH_SHORT).show();
        }
    }

    public void mapFilterToRequestURL() {
        ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu_acc, menu);
        return true;
    }


    public void onItemSelected(int i) {

        if (i == R.id.bottom_nav_learn) {
            currentFragment = this.learnFragment;
        } else if (i == R.id.bottom_nav_database) {
            currentFragment = new DatabaseFragment();
        } else if (i == R.id.bottom_nav_add_new) {
            currentFragment = new AddNewFragment();
        } else if (i == R.id.bottom_nav_search) {
            currentFragment = new SearchFragment();
        } else if (i == R.id.bottom_nav_profile) {
            currentFragment = new ProfileFragment();
        }


        loadFragment(currentFragment);

    }
}