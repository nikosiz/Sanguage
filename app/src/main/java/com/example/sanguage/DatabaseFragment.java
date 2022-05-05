package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseFragment extends Fragment {

    private ListView database_lv;
    private Long userID;
    private Context context;
    private ArrayList<String> userKnownVocab = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    public DatabaseFragment() {
    }

    public DatabaseFragment(Long userID) {
        this.userID = userID;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setKnownVocabularyListView() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://sanguage.herokuapp.com/user/getKnownVocab?userID=" + userID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setUserKnownVocab(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setUserKnownVocab(JSONObject response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String[] dictionary = mapper.readValue(response.toString(), String[].class);
            userKnownVocab.addAll(Arrays.asList(dictionary));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container, false);
        database_lv = view.findViewById(R.id.database_lv);
        setKnownVocabularyListView();
        arrayAdapter = new ArrayAdapter(this, R.layout.database_list_view_row, userKnownVocab);
        return view;

    }
}