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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.pojo.DictionaryPojo;
import com.example.sanguage.utils.FlashcardAdapter;
import com.example.sanguage.utils.ListViewAdapter;
import com.example.sanguage.utils.RequestErrorParser;
import com.example.sanguage.utils.VolleyRequestCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<DictionaryPojo> dictionaryListSimple = new ArrayList<>();
    private FlashcardAdapter flashcardAdapter;
    private Long userID;

    public SearchFragment() {
    }

    public SearchFragment(Long userID) {
        this.userID = userID;
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

    public void addKnownVocabulary(String vocabulary) {
        String addVocabURL = "https://sanguage.herokuapp.com/user/addKnownVocab?userID=" + userID + "&vocabulary=" + vocabulary;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addVocabURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = RequestErrorParser.parseError(error);
                    Log.e("addKnownVocabulary - onErrorResponse()", message);
                } catch (JSONException e) {
                    Log.e("addKnownVocabulary - onErrorResponse()", e.getMessage());
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
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
                Log.e("searchVocabStartingWithRequest - onErrorResponse()", String.valueOf(error));
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void searchGivenVocabularyRequest(String vocabulary) {
        String URL = "https://sanguage.herokuapp.com/dictionary/byLanguageVocabulary?language=English&vocabulary=" + vocabulary;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setFlashcard(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("searchGivenVocabularyRequest - onErrorResponse()", String.valueOf(error));
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setSearchResults(JSONArray response) {
        searchResults.clear();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String[] dictionary = mapper.readValue(response.toString(), String[].class);
            searchResults.addAll(Arrays.asList(dictionary));
            registerForContextMenu(search_search_lv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlashcard(JSONObject response) {
        dictionaryListSimple.clear();
        flashcardAdapter.notifyDataSetChanged();
        ObjectMapper mapper = new ObjectMapper();
        try {
            DictionaryPojo dictionaryPojo = mapper.readValue(response.toString(), DictionaryPojo.class);
            dictionaryListSimple.add(dictionaryPojo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSearchEtListener() {
        search_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    searchVocabStartingWithRequest(new VolleyRequestCallback() {
                        @Override
                        public void onSuccess() {
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }, charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setFlingContainer() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                toggleFlashcard(false);
                dictionaryListSimple.remove(0);
            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                View selectedView = flingContainer.getSelectedView();
                flashcardAdapter.toggleTranslations(selectedView, (DictionaryPojo) dataObject);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        requestQueue = Volley.newRequestQueue(context);
        search_search_et = (TextInputEditText) view.findViewById(R.id.search_search_et);
        search_search_lv = (ListView) view.findViewById(R.id.search_search_lv);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.search_flashcard);
        arrayAdapter = new ListViewAdapter(context, R.layout.database_listview_row, searchResults);
        search_search_lv.setAdapter(arrayAdapter);
        setSearchEtListener();

        flashcardAdapter = new FlashcardAdapter(context, R.layout.flashcard, dictionaryListSimple);
        flingContainer.setAdapter(flashcardAdapter);
        setFlingContainer();
        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_search, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String vocabulary = arrayAdapter.getItem(info.position);
        search_search_et.clearFocus();
        dictionaryListSimple.clear();
        flashcardAdapter.notifyDataSetChanged();
        switch (item.getItemId()) {
            case R.id.context_search_show_more:
                searchGivenVocabularyRequest(vocabulary);
                toggleFlashcard(true);
                return true;
            case R.id.context_search_add:
                Toast.makeText(getContext(), "added", Toast.LENGTH_SHORT).show();
                addKnownVocabulary(vocabulary);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void toggleFlashcard(boolean state) {
        if (state) {
            flingContainer.setEnabled(true);
            flingContainer.setVisibility(View.VISIBLE);
        } else {
            flingContainer.setEnabled(false);
            flingContainer.setVisibility(View.GONE);
        }
    }
}