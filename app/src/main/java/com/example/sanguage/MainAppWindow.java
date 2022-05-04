package com.example.sanguage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.pojo.DictionaryPojo;
import com.example.sanguage.utils.FlashcardAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainAppWindow extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_window);

        navBar = findViewById(R.id.bottomNav);
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
        inflater.inflate(R.menu.bottom_menu, menu);
        return true;
    }


    public void onItemSelected(int i) {

        Fragment fragment = null;

        if (i == R.id.bottom_nav_learn) {
            fragment = new LearnFragment();
        } else if (i == R.id.bottom_nav_database) {
            fragment = new DatabaseFragment();
        } else if (i == R.id.bottom_nav_add_new) {
            fragment = new AddNewFragment();
        } else if (i == R.id.bottom_nav_search) {
            fragment = new SearchFragment();
        } else if (i == R.id.bottom_nav_profile) {
            fragment = new ProfileFragment();
        }
        loadFragment(fragment);

    }
}