package com.example.sanguage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.codesgood.views.JustifiedTextView;
import com.example.sanguage.R;
import com.example.sanguage.pojo.DictionaryPojo;

import java.util.ArrayList;
import java.util.List;

public class FlashcardAdapter extends ArrayAdapter<DictionaryPojo> {
    private Context context;
    private ArrayList<DictionaryPojo> dictionaryList;

    public FlashcardAdapter(@NonNull Context context, int textViewResourceId, ArrayList<DictionaryPojo> dictionaryList) {
        super(context, 0, textViewResourceId, dictionaryList);
        this.context = context;
        this.dictionaryList = dictionaryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            System.out.println("1");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flashcard, parent, false);
            ConstraintLayout mainLayout = (ConstraintLayout) convertView.findViewById(R.id.flashcard_mainLayout);
            ConstraintLayout flashcardLayout = (ConstraintLayout) convertView.findViewById(R.id.flashcard_flashcardLayout);
            CardView cardView = (CardView) convertView.findViewById(R.id.flashcard_cardView);
            TextView vocabulary_tv = (TextView) convertView.findViewById(R.id.flashcard_vocabulary_tv);
/*            TextView collocations_tv = (TextView) convertView.findViewById(R.id.flashcard_collocations_tv);
            JustifiedTextView definition_jtv = (JustifiedTextView) convertView.findViewById(R.id.flashcard_definition_jtv);
            TextView topic_tv = (TextView) convertView.findViewById(R.id.flashcard_topic_tv);
            TextView gramCategory_tv = (TextView) convertView.findViewById(R.id.flashcard_grammaticalCategory_tv);*/
            DictionaryPojo dictionaryPojo = dictionaryList.get(position);
            vocabulary_tv.setText(dictionaryPojo.getVocabulary());
            convertView.setTag(new Flashcard(mainLayout, cardView, flashcardLayout, vocabulary_tv));
            /*            convertView.setTag(new Flashcard(mainLayout, cardView, flashcardLayout, vocabulary_tv, gramCategory_tv, definition_jtv, collocations_tv, topic_tv));*/
            System.out.println("2213");
            return convertView;
        } else {
/*            DictionaryPojo dictionaryPojo = dictionaryList.get(position);
            TextView vocabulary_tv = convertView.findViewById(R.id.flashcard_vocabulary_tv);
            vocabulary_tv.setText("dasddas");

            convertView.setTag(vocabulary_tv);
            System.out.println(dictionaryPojo.getVocabulary());*/
            System.out.println("else");
            return convertView;
        }

    }

    public static class Flashcard {
        ConstraintLayout mainLayout;
        CardView cardView;
        ConstraintLayout flashcardLayout;
        TextView flashcardVocabulary;
/*        TextView flashcardGramCategory;
        JustifiedTextView flashcardDefinition;
        TextView flashcardCollocations;
        TextView flashcardTopic;*/


       /* public Flashcard(ConstraintLayout mainLayout, CardView cardView, ConstraintLayout flashcardLayout, TextView flashcardVocabulary, TextView flashcardGramCategory, JustifiedTextView flashcardDefinition, TextView flashcardCollocations, TextView flashcardTopic) {
            this.mainLayout = mainLayout;
            this.cardView = cardView;
            this.flashcardLayout = flashcardLayout;
            this.flashcardVocabulary = flashcardVocabulary;
            this.flashcardGramCategory = flashcardGramCategory;
            this.flashcardDefinition = flashcardDefinition;
            this.flashcardCollocations = flashcardCollocations;
            this.flashcardTopic = flashcardTopic;
        }*/

        public Flashcard(ConstraintLayout mainLayout, CardView cardView, ConstraintLayout flashcardLayout, TextView flashcardVocabulary) {
            this.mainLayout = mainLayout;
            this.cardView = cardView;
            this.flashcardLayout = flashcardLayout;
            this.flashcardVocabulary = flashcardVocabulary;
        }
    }
}

