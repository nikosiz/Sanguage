package com.example.sanguage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import java.util.HashSet;

public class LearnFragment extends Fragment{

    private FlashcardAdapter flashcardAdapter;
    private SwipeFlingAdapterView flingContainer;
    private ArrayList<DictionaryPojo> dictionaryListSimple = new ArrayList<>();
    private Context context;
    private String currentURL;
    private RequestQueue requestQueue;
    private Long userID;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private CheckBox filter_all_vocabulary_cb, filter_vocabulary_to_revise_cb, filter_level_A1_cb, filter_level_A2_cb, filter_level_B1_cb, filter_level_B2_cb, filter_level_C1_cb, filter_level_C2_cb;
    private Button filter_apply_btn, filter_cancel_btn;
    private ImageView filter_btn;
    private HashSet<CheckBox> checkBoxHashSet;
    private View filterPopupView;

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

    public void setFlingContainer() {
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                dictionaryListSimple.remove(0);
                flashcardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                if (userID != null) {
                    DictionaryPojo dictionaryPojo = (DictionaryPojo) o;
                    deleteUserKnownVocabRequest(dictionaryPojo.getVocabularyTranslated());
                }
            }

            @Override
            public void onRightCardExit(Object o) {
                if (userID != null) {
                    DictionaryPojo dictionaryPojo = (DictionaryPojo) o;
                    addVocabularyToLearn(dictionaryPojo.getVocabularyTranslated());
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                dictionaryRequest();
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

    public void addVocabularyToLearn(String vocabularyTranslated) {
        String addVocabURL = "https://sanguage.herokuapp.com/user/addKnownVocab?userID=" + userID + "&vocabulary=" + vocabularyTranslated;
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

    public void dictionaryRequest() {
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
        dialogBuilder = new AlertDialog.Builder(getContext());
        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog();
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.learn_frame_flashcard);
        currentURL = "https://sanguage.herokuapp.com/dictionary/mixedByLanguage?language=English";
        flashcardAdapter = new FlashcardAdapter(context, R.layout.flashcard, dictionaryListSimple);
        flingContainer.setAdapter(flashcardAdapter);
        setFlingContainer();
        return view;
    }

    public void initializeFilterPopupView() {
        filterPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        filter_all_vocabulary_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_all_vocabulary_cb);
        filter_vocabulary_to_revise_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_vocabulary_to_revise_cb);
        filter_level_A1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_A1_cb);
        filter_level_A2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_A2_cb);
        filter_level_B1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_B1_cb);
        filter_level_B2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_B2_cb);
        filter_level_C1_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_C1_cb);
        filter_level_C2_cb = (CheckBox) filterPopupView.findViewById(R.id.filter_level_C2_cb);
        filter_all_vocabulary_cb.setChecked(true);
        filter_all_vocabulary_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    filter_vocabulary_to_revise_cb.setChecked(true);
                } else {
                    filter_vocabulary_to_revise_cb.setChecked(false);
                    compoundButton.setChecked(b);
                }
            }
        });
        filter_vocabulary_to_revise_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    filter_all_vocabulary_cb.setChecked(true);
                } else {
                    filter_all_vocabulary_cb.setChecked(false);
                    compoundButton.setChecked(b);
                }
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = preferences.getBoolean("enabled", false);
        if (!enabled) {
            filter_all_vocabulary_cb.setEnabled(false);
            filter_vocabulary_to_revise_cb.setEnabled(false);
            filter_all_vocabulary_cb.setAlpha(0.5f);
            filter_vocabulary_to_revise_cb.setAlpha(0.5f);
        }
        checkBoxHashSet = new HashSet<>();
        checkBoxHashSet.add(filter_level_A1_cb);
        checkBoxHashSet.add(filter_level_A2_cb);
        checkBoxHashSet.add(filter_level_B1_cb);
        checkBoxHashSet.add(filter_level_B2_cb);
        checkBoxHashSet.add(filter_level_C1_cb);
        checkBoxHashSet.add(filter_level_C2_cb);
        filter_apply_btn = (Button) filterPopupView.findViewById(R.id.filter_apply_btn);
        filter_cancel_btn = (Button) filterPopupView.findViewById(R.id.filter_cancel_btn);
        dialogBuilder.setView(filterPopupView);
        dialog = dialogBuilder.create();
    }

    public void filterButtonsHandle() {
        filter_apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFilterToURL();
                flashcardAdapter.clear();
                dialog.dismiss();
            }
        });

        filter_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void filterDialog() {
        if (filterPopupView == null) {
            initializeFilterPopupView();
        }
        dialog.show();
        filterButtonsHandle();
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

    public void mapFilterToURL() {
        currentURL = "";
        boolean allVocab = filter_all_vocabulary_cb.isChecked();
        boolean VocabToRevise = filter_vocabulary_to_revise_cb.isChecked();
        FilterState state;
        if (allVocab) {
            state = FilterState.MIXED;
        } else {
            state = FilterState.KNOWN;
        }
        ArrayList<String> checkedLevels = new ArrayList<>();
        for (CheckBox checkBox : checkBoxHashSet) {
            if (checkBox.isChecked()) {
                checkedLevels.add(checkBox.getText().toString());
            }
        }
        if (state.equals(FilterState.KNOWN) && checkedLevels.isEmpty()) {
            currentURL = "https://sanguage.herokuapp.com/dictionary/knownByLanguage?language=English&userID=" + userID;
        } else if (state.equals(FilterState.MIXED) && checkedLevels.isEmpty()) {
            currentURL = "https://sanguage.herokuapp.com/dictionary/mixedByLanguage?language=English";
        } else if (state.equals(FilterState.NEW) && checkedLevels.isEmpty()) {
            currentURL = "https://sanguage.herokuapp.com/dictionary/newByLanguage?language=English&userID=" + userID;
        } else {
            mapStateLevelsToURL(checkedLevels, state);
        }
    }

    public void mapStateLevelsToURL(ArrayList<String> checkedLevels, FilterState state) {
        int hashSetSize = checkedLevels.size();
        if (state.equals(FilterState.KNOWN)) {
            currentURL = "https://sanguage.herokuapp.com/dictionary/knownByLanguage";
        } else {
            currentURL = "https://sanguage.herokuapp.com/dictionary/mixedByLanguage";
        }
        if (hashSetSize == 1) {
            String level = checkedLevels.get(0);
            currentURL = currentURL + "Level?language=English&level=" + level;
        } else if (hashSetSize == 2) {
            String level1 = checkedLevels.get(0);
            String level2 = checkedLevels.get(1);
            currentURL = currentURL + "TwoLevel?language=English&level1=" + level1 + "&level2=" + level2;
        } else if (hashSetSize == 3) {
            String level1 = checkedLevels.get(0);
            String level2 = checkedLevels.get(1);
            String level3 = checkedLevels.get(2);
            currentURL = currentURL + "ThreeLevel?language=English&level1=" + level1 + "&level2=" + level2 + "&level3=" + level3;
        } else if (hashSetSize == 4) {
            String level1 = checkedLevels.get(0);
            String level2 = checkedLevels.get(1);
            String level3 = checkedLevels.get(2);
            String level4 = checkedLevels.get(3);
            currentURL = currentURL + "FourLevel?language=English&level1=" + level1 + "&level2=" + level2 + "%level3=" + level3 + "&level4=" + level4;
        } else if (hashSetSize == 5) {
            String level1 = checkedLevels.get(0);
            String level2 = checkedLevels.get(1);
            String level3 = checkedLevels.get(2);
            String level4 = checkedLevels.get(3);
            String level5 = checkedLevels.get(4);
            currentURL = currentURL + "FiveLevel?language=English&level1=" + level1 + "&level2=" + level2 + "%level3=" + level3 + "&level4=" + level4 + "&level5=" + level5;
        } else if (hashSetSize == 6) {
            currentURL = currentURL + "?language=English&";
        }
        if (state.equals(FilterState.KNOWN) || state.equals(FilterState.NEW)) {
            currentURL = currentURL + "&userID=" + userID;
        }
    }

    enum FilterState {
        KNOWN, NEW, MIXED
    }
}
