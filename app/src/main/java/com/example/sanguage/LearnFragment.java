package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LearnFragment extends Fragment {

    private FlashcardAdapter flashcardAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<DictionaryPojo> dictionaryListSimple = new ArrayList<>();
    private Context context;
    private String currentURL;


    public LearnFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static LearnFragment newInstance() {
        LearnFragment fragment = new LearnFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_learn);
    }

    private void setContentView(int fragment_learn) {
    }

    public void setFlingContainer() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                dictionaryListSimple.remove(0);
                flashcardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
            }

            @Override
            public void onRightCardExit(Object o) {
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                dictionaryRequest();
            }

            @Override
            public void onScroll(float v) {
                ;
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                System.out.println("onitemclicked");

            }
        });
    }

    public void dictionaryRequest() {
        System.out.println("dictionary request");
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, currentURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseJSONArray(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void addDictionaryListSimple(ArrayList<DictionaryPojo> dictionaryListSimple) {
        this.dictionaryListSimple.addAll(dictionaryListSimple);
        this.flashcardAdapter.notifyDataSetChanged();
    }

    public void setDictionaryListSimple(ArrayList<DictionaryPojo> dictionaryListSimple) {
        this.dictionaryListSimple = dictionaryListSimple;
    }

    public void parseJSONArray(JSONArray response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DictionaryPojo[] dictionary = mapper.readValue(response.toString(), DictionaryPojo[].class);
            addDictionaryListSimple(new ArrayList<>(Arrays.asList(dictionary)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame_flashcard);
        currentURL = "https://sanguage.herokuapp.com/dictionary/byLanguage?language=English";
        flashcardAdapter = new FlashcardAdapter(context, R.layout.flashcard, dictionaryListSimple);
        flingContainer.setAdapter(flashcardAdapter);
        setFlingContainer();
        return view;
    }
}