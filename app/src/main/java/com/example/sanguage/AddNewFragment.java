package com.example.sanguage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sanguage.databinding.FragmentAddNewBinding;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewFragment<adapter> extends Fragment {

    String[] grammatical_category = {"Adjective", "Adverb", "Conjunction", "Determiner",
            "Interjection", "Noun", "Number", "Phrasal verb", "Phrase", "Prefix", "Preposition",
            "Pronoun", "Verb"};

    AutoCompleteTextView add_new_grammatical_category_actv;
    ArrayAdapter<String> adapterItems;

    private FragmentAddNewBinding binding;
    private TextInputEditText add_new_vocabulary_et;
    private TextInputEditText add_new_definition_et;
    private TextInputEditText add_new_collocations_et;
    private TextInputEditText add_new_vocabulary_translation_et;
    private TextInputEditText add_new_collocations_translation_et;
    private RadioGroup add_new_rg;
    private Button add_new_next_btn;

    public AddNewFragment() {
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

        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.dropdown, grammatical_category);

        add_new_grammatical_category_actv.setAdapter(adapterItems);

        add_new_grammatical_category_actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getContext(), "Item:"+item, Toast.LENGTH_SHORT);
            }
        });


        add_new_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vocabulary = add_new_vocabulary_et.getText().toString();
                String definition = add_new_definition_et.getText().toString();
                String collocations = add_new_collocations_et.getText().toString();
                String vocabularyTranslated = add_new_vocabulary_translation_et.getText().toString();
                String collocationsTranslated = add_new_collocations_translation_et.getText().toString();

            }
        });

        return view;
    }
}