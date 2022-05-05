package com.example.sanguage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment {

    private ListView database_lv;

    public SearchFragment() {
    }

    public static DatabaseFragment newInstance(String param1, String param2) {
        DatabaseFragment fragment = new DatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        TextView asdf = (TextView) view.findViewById(R.id.search_vocabulary_tv);

        registerForContextMenu(asdf);

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.context_search, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_search_show_more:
                Toast.makeText(getContext(), "showing more", Toast.LENGTH_SHORT).show();
                //show selected vocabulary on a flashcard
                return true;
            case R.id.context_search_add:
                Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
                //delete selected vocabulary from database
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}