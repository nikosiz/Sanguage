package com.example.sanguage;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.pojo.DictionaryPojo;
import com.example.sanguage.utils.FlashcardAdapter;
import com.example.sanguage.utils.RequestErrorParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LearnFragment extends Fragment {

    private FlashcardAdapter flashcardAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<DictionaryPojo> dictionaryListSimple = new ArrayList<>();
    private Context context;
    private String currentURL;
    private RequestQueue requestQueue;
    private Long userID;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView filter_known_words_tv, filter_new_words_tv, filter_level_tv, filter_topic_tv;
    private CheckBox filter_known_words_cb, filter_new_words_cb, filter_level_A1_cb, filter_level_A2_cb, filter_level_B1_cb, filter_level_B2_cb, filter_level_C1_cb, filter_level_C2_cb, filter_topic_cb;
    private Button filter_apply_btn, filter_cancel_btn;
    private ImageView filter_btn;

    public LearnFragment(Long userID) {
        this.userID = userID;
    }

    public LearnFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
                DictionaryPojo dictionaryPojo = (DictionaryPojo) o;
                addKnownVocabulary(dictionaryPojo.getVocabularyTranslated());
            }

            @Override
            public void onRightCardExit(Object o) {
                ;
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
                View selectedView = flingContainer.getSelectedView();
                System.out.println(itemPosition);
                flashcardAdapter.toggleTranslations(selectedView,(DictionaryPojo) dataObject);
            }
        });
    }

    public void addKnownVocabulary(String vocabularyTranslated) {
        String addVocabURL = "https://sanguage.herokuapp.com/user/addKnownVocab?userID=" + userID + "&vocabulary=" + vocabularyTranslated;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addVocabURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
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

    public void dictionaryRequest() {
        System.out.println("dictionary request");
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
        requestQueue.add(jsonArrayRequest);
    }

    public void addDictionaryListSimple(ArrayList<DictionaryPojo> dictionaryListSimple) {
        this.dictionaryListSimple.addAll(dictionaryListSimple);
        this.flashcardAdapter.notifyDataSetChanged();
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

        filter_btn = (ImageView) view.findViewById(R.id.learn_filter_btn);

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog();
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame_flashcard);
        currentURL = "https://sanguage.herokuapp.com/dictionary/byLanguage?language=English";
        flashcardAdapter = new FlashcardAdapter(context, R.layout.flashcard, dictionaryListSimple);
        flingContainer.setAdapter(flashcardAdapter);
        setFlingContainer();
        return view;
    }

    public void filterDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View filterPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        filter_known_words_tv = (TextView) filterPopupView.findViewById(R.id.filter_known_words_tv);
        filter_new_words_tv = (TextView) filterPopupView.findViewById(R.id.filter_new_words_tv);
        filter_level_tv = (TextView) filterPopupView.findViewById(R.id.filter_level_tv);
        filter_topic_tv = (TextView) filterPopupView.findViewById(R.id.filter_topic_tv);

        filter_known_words_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_known_words_cb);
        filter_new_words_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_new_words_cb);
        filter_level_A1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_A1_cb);
        filter_level_A2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_A2_cb);
        filter_level_B1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_B1_cb);
        filter_level_B2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_B2_cb);
        filter_level_C1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_C1_cb);
        filter_level_C2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_C2_cb);
        filter_topic_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_topic_cb);

        filter_apply_btn = (Button) filterPopupView.findViewById(R.id.filter_apply_btn);
        filter_cancel_btn = (Button) filterPopupView.findViewById(R.id.filter_cancel_btn);

        dialogBuilder.setView(filterPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        filter_apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apply filter action
            }
        });

        filter_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apply cancel action
                dialog.dismiss();
            }
        });
    }
}