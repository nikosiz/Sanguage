package com.example.sanguage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.databinding.ActivityAppWindowBinding;
import com.example.sanguage.pojo.DictionaryPojo;
import com.example.sanguage.utils.RequestErrorParser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppWindowAccount extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private ChipNavigationBar navBar;
    private LearnFragment learnFragment;
    private DatabaseFragment databaseFragment;
    private Fragment currentFragment;
    private Long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_window);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new AddNewFragment());
        fragmentTransaction.commit();
        navBar = findViewById(R.id.bottomNav);
        currentFragment = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID = preferences.getLong("userID", 9999999999999L);
        databaseFragment = new DatabaseFragment(userID);
        learnFragment = new LearnFragment(userID,databaseFragment);
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
            currentFragment = this.databaseFragment;
        } else if (i == R.id.bottom_nav_add_new) {
            currentFragment = new AddNewFragment();
        } else if (i == R.id.bottom_nav_search) {
            currentFragment = new SearchFragment();
        } else if (i == R.id.bottom_nav_profile) {
            currentFragment = new ProfileFragment(userID);
        }

        loadFragment(currentFragment);

    }
}