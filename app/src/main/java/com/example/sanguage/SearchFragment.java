package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.utils.ListViewAdapter;
import com.example.sanguage.utils.VolleyRequestCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment {

    private TextInputEditText search_search_et;
    private ListView search_search_lv;
    private RequestQueue requestQueue;
    private Context context;
    private ListViewAdapter arrayAdapter;
    private ArrayList<String> searchResults = new ArrayList<>();

    public SearchFragment() {
    }

    public static DatabaseFragment newInstance(String param1, String param2) {
        DatabaseFragment fragment = new DatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public void searchVocabStartingWithRequest(final VolleyRequestCallback callback, String searchFor) {
        String URL = "https://sanguage.herokuapp.com/dictionary/searchSimilarVocabulary?language=English&searchFor=" + searchFor;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                setSearchResults(response);
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("knownVocabularyRequest - onErrorResponse()", String.valueOf(error));
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void setSearchResults(JSONArray response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String[] dictionary = mapper.readValue(response.toString(), String[].class);
            searchResults.addAll(Arrays.asList(dictionary));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*    public void setSearchEtListener() {
        search_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchVocabStartingWithRequest(new VolleyRequestCallback() {
                    @Override
                    public void onSuccess() {
                        arrayAdapter = new ListViewAdapter(context, R.layout.database_listview_row, searchResults);
                        search_database_lv.setAdapter(arrayAdapter);
                    }
                }, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        requestQueue = Volley.newRequestQueue(context);
        search_search_et = (TextInputEditText) view.findViewById(R.id.search_search_et);
        //search_search_lv= (ListView) view.findViewById(R.id.search_search_lv);
      //  setSearchEtListener();
        registerForContextMenu(search_search_et);
        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.context_search, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_search_show_more:
                Toast.makeText(getContext(), "showing more", Toast.LENGTH_SHORT).show();
                //show selected vocabulary on a flashcard
                return true;
            case R.id.context_search_add:
                Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
                //delete selected vocabulary from database
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}