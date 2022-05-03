package com.example.sanguage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.chip.Chip;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainAppWindow extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener {

    private List vocabulary;
    private ArrayAdapter<String> arrayAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ChipNavigationBar navigationBar;
    private FlashcardAdapter flashcardAdapter;
    private String currentURL;
    private ArrayList<DictionaryPojo> dictionaryListSimple;
    private ArrayList<DictionaryPojo> dictionaryListBuffer;

    public enum DictionaryListEnum {SIMPLE, BUFFER}

    //TODO supply buffer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_window);
        flingContainer = findViewById(R.id.frame_flashcard);
        dictionaryListSimple = new ArrayList<>();
        dictionaryListBuffer = new ArrayList<>();
        setFlingContainer();

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainAppWindow.this, "< Swipe right to learn | Swipe left to reject >", Toast.LENGTH_SHORT).show();

            }
        });

        navigationBar = findViewById(R.id.bottomNav);
        //navigationBar.setItemSelected(R.id.bottom_nav_learn, true);
        //navigationBar.setOnItemSelectedListener(this);
        navigationBar.setItemSelected(R.id.bottom_nav_learn, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LearnFragment()).commit();
        bottomMenu();

    }

    private void bottomMenu() {

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_learn:
                        fragment = new LearnFragment();
                        break;
                    case R.id.bottom_nav_database:
                        fragment = new DatabaseFragment();
                        break;
                    case R.id.bottom_nav_add_new:
                        fragment = new AddNewFragment();
                        break;
                    case R.id.bottom_nav_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

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

    public void mapFilterToRequestURL() {
        ;
    }

    public void dictionaryRequest(DictionaryListEnum i) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, currentURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    DictionaryPojo[] dictionary = mapper.readValue(response.toString(), DictionaryPojo[].class);
                    if (i.equals(DictionaryListEnum.SIMPLE)) {
                        dictionaryListSimple.addAll(Arrays.asList(dictionary));
                    } else {
                        dictionaryListBuffer.addAll(Arrays.asList(dictionary));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (i.equals(DictionaryListEnum.SIMPLE)) {
                    dictionaryListSimple.addAll(dictionaryListBuffer);
                }
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void updateFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        if (flashcardAdapter == null) {
            createFlashcardAdapter(dictionaryListSimple);
        } else {
            setFlashcardAdapter(dictionaryListSimple);
        }
    }

    public void createFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        FlashcardAdapter flashcardAdapter = new FlashcardAdapter(this, dictList);
        flingContainer.setAdapter(flashcardAdapter);
    }

    public void setFlashcardAdapter(ArrayList<DictionaryPojo> dictList) {
        flashcardAdapter.clear();
        flashcardAdapter.addAll(dictList);
    }

    public void setFlingContainer() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                ;
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(MainAppWindow.this, "Rejected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(MainAppWindow.this, "Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                if (i == 7) {
                    dictionaryRequest(DictionaryListEnum.SIMPLE);
                }
            }

            @Override
            public void onScroll(float v) {
                ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu, menu);
        return true;
    }

    public void disableFlingContainer() {
        flingContainer.setEnabled(false);
        flingContainer.setVisibility(View.INVISIBLE);
    }

    public void enableFlingContainer() {
        flingContainer.setEnabled(true);
        flingContainer.setVisibility(View.VISIBLE);
    }

    public void onItemSelected(int i) {

    }
}