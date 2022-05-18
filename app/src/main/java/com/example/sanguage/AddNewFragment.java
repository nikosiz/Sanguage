package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.databinding.FragmentAddNewBinding;
import com.example.sanguage.utils.RequestErrorParser;
import com.example.sanguage.utils.VolleyRequestCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewFragment<adapter> extends Fragment {

    String[] grammatical_category = {"adjective", "adverb", "conjunction", "determiner",
            "interjection", "noun", "number", "phrasal verb", "phrase", "prefix", "preposition",
            "pronoun", "verb"};

    AutoCompleteTextView add_new_grammatical_category_actv;
    ArrayAdapter<String> adapterItems;

    private FragmentAddNewBinding binding;
    private TextInputEditText add_new_vocabulary_et;
    private TextInputEditText add_new_definition_et;
    private TextInputEditText add_new_collocations_et;
    private TextInputEditText add_new_vocabulary_translation_et;
    private TextInputEditText add_new_collocations_translation_et;
    private Button add_new_next_btn;
    private Context context;

    public AddNewFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new, container, false);
        add_new_next_btn = (Button) view.findViewById(R.id.add_new_add_btn);
        add_new_vocabulary_et = view.findViewById(R.id.add_new_vocabulary_et);
        add_new_definition_et = view.findViewById(R.id.add_new_definition_et);
        add_new_collocations_et = view.findViewById(R.id.add_new_collocations_et);
        add_new_vocabulary_translation_et = view.findViewById(R.id.add_new_vocabulary_translation_et);
        add_new_collocations_translation_et = view.findViewById(R.id.add_new_collocations_translation_et);

        add_new_grammatical_category_actv = (AutoCompleteTextView) view.findViewById(R.id.add_new_gram_cat_actv);

        adapterItems = new ArrayAdapter<String>(context, R.layout.dropdown, grammatical_category);

        add_new_grammatical_category_actv.setAdapter(adapterItems);

        add_new_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAddBtn(false);
                String vocabulary = add_new_vocabulary_et.getText().toString();
                String definition = add_new_definition_et.getText().toString();
                String collocations = add_new_collocations_et.getText().toString();
                String vocabularyTranslated = add_new_vocabulary_translation_et.getText().toString();
                String collocationsTranslated = add_new_collocations_translation_et.getText().toString();
                String grammaticalCategory = add_new_grammatical_category_actv.getText().toString();
                // TODO letters only
                if (vocabulary.isEmpty() || definition.isEmpty() || collocations.isEmpty() || vocabularyTranslated.isEmpty() || collocationsTranslated.isEmpty()) {
                    Toast.makeText(context, "provide all data", Toast.LENGTH_SHORT).show();
                    toggleAddBtn(true);
                } else if (grammaticalCategory.equals("Grammatical category")) {
                    Toast.makeText(context, "provide grammatical category", Toast.LENGTH_SHORT).show();
                    toggleAddBtn(true);
                } else {
                    String grammaticalCategoryTranslated = translateGrammaticalCategory(grammaticalCategory);
                    try {
                        JSONObject addNewJSON = createAddNewJSON(grammaticalCategoryTranslated, vocabulary, definition, collocations, vocabularyTranslated, collocationsTranslated, grammaticalCategory);
                        addNewRequest(addNewJSON, new VolleyRequestCallback() {
                            @Override
                            public void onSuccess() {
                                toggleAddBtn(true);
                                add_new_vocabulary_et.getText().clear();
                                add_new_definition_et.getText().clear();
                                add_new_collocations_et.getText().clear();
                                add_new_vocabulary_translation_et.getText().clear();
                                add_new_collocations_translation_et.getText().clear();
                                Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    public String translateGrammaticalCategory(String grammaticalCategory) {
        switch (grammaticalCategory) {
            case "adjective":
                return "przymiotnik";
            case "adverb":
                return "przysłówek";

            case "conjunction":
                return "spójnik";

            case "determiner":
                return "określnik";

            case "interjection":
                return "wykrzyknik";

            case "noun":
                return "rzeczownik";

            case "number":
                return "liczba";

            case "phrasal verb":
                return "czasownik złożony";

            case "phrase":
                return "fraza";

            case "prefix":
                return "przedrostek";

            case "preposition":
                return "przyimek";

            case "pronoun":
                return "zaimek";
            case "verb":
                return "czasownik";
            default:
                return "";

        }
    }

    public JSONObject createAddNewJSON(String grammaticalCategoryTranslated, String vocabulary, String definition, String collocations, String vocabularyTranslated, String collocationsTranslated, String grammaticalCategory) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vocabulary", vocabularyTranslated);
        jsonObject.put("grammaticalCategory", grammaticalCategoryTranslated);
        jsonObject.put("collocations", collocationsTranslated);
        jsonObject.put("vocabularyTranslated", vocabulary);
        jsonObject.put("grammaticalCategoryTranslated", grammaticalCategory);
        jsonObject.put("definitionTranslated", definition);
        jsonObject.put("collocationsTranslated", collocationsTranslated);
        jsonObject.put("language", "English");
        jsonObject.put("enabled", false);
        return jsonObject;
    }

    public void addNewRequest(JSONObject jsonObject, final VolleyRequestCallback callback) {
        String URL = "https://sanguage.herokuapp.com/addWordToConfirm";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String message = RequestErrorParser.parseError(error);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                Log.e("addNewRequest - onErrorResponse()", String.valueOf(error));
                toggleAddBtn(true);
            }
        }
        );
        queue.add(jsonObjectRequest);
    }

    public void toggleAddBtn(boolean state) {
        add_new_next_btn.setEnabled(state);
    }
}
