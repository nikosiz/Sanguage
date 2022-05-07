package com.example.sanguage.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sanguage.R;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> implements Filterable {
    private Context context;
    private ArrayList<String> filteredItemsList;
    private ArrayList<String> allItemsList = new ArrayList<>();

    public ListViewAdapter(@NonNull Context context, int textViewResourceId, ArrayList<String> filteredItemsList) {
        super(context, 0, textViewResourceId, filteredItemsList);
        this.context = context;
        this.filteredItemsList = filteredItemsList;
        allItemsList.addAll(filteredItemsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.database_listview_row, parent, false);
            String item = filteredItemsList.get(position);
            TextView textView = convertView.findViewById(R.id.list_view_row);
            textView.setText(item);
            convertView.setTag(textView);
            return convertView;
        } else {
            String item = filteredItemsList.get(position);
            TextView textView = convertView.findViewById(R.id.list_view_row);
            textView.setText(item);
            convertView.setTag(textView);
            return convertView;
        }
    }
    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItemsList.clear();
                filteredItemsList.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredArrList = new ArrayList<String>();

                if (constraint == null || constraint.length() == 0) {
                    results.count = allItemsList.size();
                    results.values = allItemsList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allItemsList.size(); i++) {
                        String data = allItemsList.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;

    }

    public void remove(String vocabulary) {
        allItemsList.remove(vocabulary);
        filteredItemsList.remove(vocabulary);
        notifyDataSetChanged();
    }
}
