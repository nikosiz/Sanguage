package com.example.sanguage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sanguage.R;
import com.example.sanguage.pojo.DictionaryPojo;

import java.util.ArrayList;

public class FlashcardAdapter extends ArrayAdapter<DictionaryPojo> {
    private Context context;
    private ArrayList<DictionaryPojo> dictionary;
    private boolean translated;

    public FlashcardAdapter(@NonNull Context context, int textViewResourceId, ArrayList<DictionaryPojo> dictionary) {
        super(context, 0, textViewResourceId, dictionary);
        this.context = context;
        this.dictionary = dictionary;
        notifyDataSetChanged();
    }

    public void toggleTranslations(View view, DictionaryPojo dictionaryPojo) {
        TextView vocabulary_tv = (TextView) view.findViewById(R.id.search_vocabulary_tv);
        TextView collocations_tv = (TextView) view.findViewById(R.id.search_collocations_tv);
        TextView definition_jtv = (TextView) view.findViewById(R.id.search_definition_jtv);
        TextView gramCategory_tv = (TextView) view.findViewById(R.id.search_grammaticalCategory_tv);
        if (translated) {
            translated=false;
            vocabulary_tv.setText(dictionaryPojo.getVocabulary().replaceAll(";"," ").replaceAll(",","\n"));
            collocations_tv.setText(dictionaryPojo.getCollocations());
            definition_jtv.setText("");
            gramCategory_tv.setText(dictionaryPojo.getGrammaticalCategory());
        } else {
            translated = true;
            vocabulary_tv.setText(dictionaryPojo.getVocabularyTranslated());
            gramCategory_tv.setText(dictionaryPojo.getGrammaticalCategoryTranslated());
            definition_jtv.setText(dictionaryPojo.getDefinitionTranslated());
            collocations_tv.setText(dictionaryPojo.getCollocationsTranslated().replaceAll(";", ""));
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            translated = true;
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard, parent, false);
            ConstraintLayout mainLayout = (ConstraintLayout) convertView.findViewById(R.id.flashcard_mainLayout);
            ConstraintLayout flashcardLayout = (ConstraintLayout) convertView.findViewById(R.id.flashcard_flashcardLayout);
            CardView cardView = (CardView) convertView.findViewById(R.id.flashcard_cardView);
            TextView vocabulary_tv = (TextView) convertView.findViewById(R.id.search_vocabulary_tv);
            TextView collocations_tv = (TextView) convertView.findViewById(R.id.search_collocations_tv);
            TextView definition_jtv = (TextView) convertView.findViewById(R.id.search_definition_jtv);
            TextView gramCategory_tv = (TextView) convertView.findViewById(R.id.search_grammaticalCategory_tv);
            DictionaryPojo dictionaryPojo = dictionary.get(position);
            vocabulary_tv.setText(dictionaryPojo.getVocabularyTranslated());
            gramCategory_tv.setText(dictionaryPojo.getGrammaticalCategoryTranslated());
            definition_jtv.setText(dictionaryPojo.getDefinitionTranslated());
            collocations_tv.setText(dictionaryPojo.getCollocationsTranslated().replaceAll(";", ""));
            convertView.setTag(new Flashcard(mainLayout, cardView, flashcardLayout, vocabulary_tv, gramCategory_tv, definition_jtv, collocations_tv));
            return convertView;
        } else {
            return convertView;
        }
    }

    public class Flashcard {
        ConstraintLayout mainLayout;
        CardView cardView;
        ConstraintLayout flashcardLayout;
        TextView flashcardVocabulary;
        TextView flashcardGramCategory;
        TextView flashcardDefinition;
        TextView flashcardCollocations;


        public Flashcard(ConstraintLayout mainLayout, CardView cardView, ConstraintLayout flashcardLayout, TextView flashcardVocabulary, TextView flashcardGramCategory, TextView flashcardDefinition, TextView flashcardCollocations) {
            this.mainLayout = mainLayout;
            this.cardView = cardView;
            this.flashcardLayout = flashcardLayout;
            this.flashcardVocabulary = flashcardVocabulary;
            this.flashcardGramCategory = flashcardGramCategory;
            this.flashcardDefinition = flashcardDefinition;
            this.flashcardCollocations = flashcardCollocations;
        }


    }
}

