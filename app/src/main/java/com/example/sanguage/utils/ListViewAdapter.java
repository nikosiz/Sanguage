package com.example.sanguage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.codesgood.views.JustifiedTextView;
import com.example.sanguage.R;
import com.example.sanguage.pojo.DictionaryPojo;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> itemsList;

    public ListViewAdapter(@NonNull Context context, int textViewResourceId, ArrayList<String> itemsList) {
        super(context, 0, textViewResourceId, itemsList);
        this.context = context;
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.database_listview_row, parent, false);
            String item = itemsList.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.list_view_row);
            textView.setText(item);
            convertView.setTag(textView);
            return convertView;
        } else {
            return convertView;
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
