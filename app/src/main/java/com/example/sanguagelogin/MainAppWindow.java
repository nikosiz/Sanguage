package com.example.sanguagelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainAppWindow extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    List vocabulary;
    private ArrayAdapter<String> arrayAdapter;
    SwipeFlingAdapterView flingContainer;


    ChipNavigationBar navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_window);

        loadFragment(new LearnFragment());

        flingContainer = findViewById(R.id.frame_flashcard);

        vocabulary = new ArrayList();
        vocabulary.add("1");
        vocabulary.add("2");
        vocabulary.add("3");
        vocabulary.add("4");

        arrayAdapter = new ArrayAdapter<>(this, R.layout.flashcard, R.id.vocabulary_fc, vocabulary);

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                vocabulary.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(MainAppWindow.this, "Left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(MainAppWindow.this, "Right", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                vocabulary.add("XML".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                i++;

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.swipe_left_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainAppWindow.this, "ASDFASDF", Toast.LENGTH_SHORT).show();

            }
        });

        navBar = findViewById(R.id.bottomNav);
        navBar.setOnItemSelectedListener(this);
    }

    private void left(View v) {
        flingContainer.getTopCardListener().selectLeft();
    }

    private void right(View v) {
        flingContainer.getTopCardListener().selectRight();
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