package com.example.sanguage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sanguage.R;
import com.example.sanguage.pojo.DictionaryPojo;

import java.util.ArrayList;

public class FlashcardAdapter extends ArrayAdapter<DictionaryPojo> {
    private Context mContext;
    private ArrayList<DictionaryPojo> dictionaryList;

    public FlashcardAdapter(@NonNull Context context, ArrayList<DictionaryPojo> dictionaryList) {
        super(context, 0, dictionaryList);
        this.mContext = context;
        this.dictionaryList = dictionaryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DictionaryPojo currentDict = dictionaryList.get(position);
        TextView vocabulary_tv = (TextView) convertView.findViewById(R.id.flashcard_vocabulary_tv);
        vocabulary_tv.setText(currentDict.getVocabulary());
        return super.getView(position, convertView, parent);
    }

}
