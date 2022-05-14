package com.example.sanguage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sanguage.databinding.FragmentAddNewBinding;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewFragment<adapter> extends Fragment {

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
        add_new_vocabulary_et=view.findViewById(R.id.add_new_vocabulary_et);
        add_new_definition_et=view.findViewById(R.id.add_new_definition_et);
        add_new_collocations_et=view.findViewById(R.id.add_new_collocations_et);
        add_new_vocabulary_translation_et=view.findViewById(R.id.add_new_vocabulary_translation_et);
        add_new_collocations_translation_et=view.findViewById(R.id.add_new_collocations_translation_et);
        add_new_rg=view.findViewById(R.id.add_new_rg);


        add_new_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vocabulary=add_new_vocabulary_et.getText().toString();
                String definition=add_new_definition_et.getText().toString();
                String collocations=add_new_collocations_et.getText().toString();
                String vocabularyTranslated=add_new_vocabulary_translation_et.getText().toString();
                String collocationsTranslated=add_new_collocations_translation_et.getText().toString();
                int checkedRadioButtonId = add_new_rg.getCheckedRadioButtonId();

            }
        });

        return view;
    }
}