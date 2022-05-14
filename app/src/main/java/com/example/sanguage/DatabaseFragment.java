package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.utils.ListViewAdapter;
import com.example.sanguage.utils.RequestErrorParser;
import com.example.sanguage.utils.VolleyRequestCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseFragment extends Fragment {

    private ListView database_lv;
    private Long userID;
    private Context context;
    private ArrayList<String> userKnownVocab = new ArrayList<>();
    private ListViewAdapter arrayAdapter;
    private TextInputEditText databaseSearch_et;
    private RequestQueue requestQueue;

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

    public void userKnownVocabRequest(final VolleyRequestCallback callback) {
        String URL = "https://sanguage.herokuapp.com/user/getKnownVocab?userID=" + userID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                setUserKnownVocab(response);
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

    public void setUserKnownVocab(JSONArray response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String[] dictionary = mapper.readValue(response.toString(), String[].class);
            userKnownVocab.addAll(Arrays.asList(dictionary));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDatabaseSearchListener() {
        databaseSearch_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container, false);
        database_lv = view.findViewById(R.id.database_lv);
        databaseSearch_et = view.findViewById(R.id.database_search_et);
        requestQueue = Volley.newRequestQueue(context);
        registerForContextMenu(database_lv);
        setDatabaseSearchListener();
        if (userKnownVocab.isEmpty()) {
            userKnownVocabRequest(new VolleyRequestCallback() {
                @Override
                public void onSuccess() {
                    arrayAdapter = new ListViewAdapter(context, R.layout.database_listview_row, userKnownVocab);
                    database_lv.setAdapter(arrayAdapter);
                }
            });
        }
        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_database, menu);
    }

    public void deleteUserKnownVocab(String vocabulary) {
        arrayAdapter.remove(vocabulary);
    }

    public void deleteUserKnownVocabRequest(String vocabulary) {
        String URL = "https://sanguage.herokuapp.com/user/deleteKnownVocab?userID=" + userID + "&vocabulary=" + vocabulary;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("deleteUserKnownVocabRequest - onErrorResponse()", String.valueOf(error));
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_database_show_more:
                Toast.makeText(getContext(), "show more" + item.getItemId(), Toast.LENGTH_SHORT).show();
                //show selected vocabulary on a flashcard
                String dw = arrayAdapter.getItem(info.position);
                System.out.println(dw);
                return true;
            case R.id.context_database_delete:
                Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                String vocabulary = arrayAdapter.getItem(info.position);
                deleteUserKnownVocab(vocabulary);
                deleteUserKnownVocabRequest(vocabulary);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
