package com.example.sanguage;

import static androidx.core.view.ViewCompat.getTransitionName;

import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sanguage.databinding.FragmentAddNewBinding;

public class AddNewFragment<adapter> extends Fragment {

    private FragmentAddNewBinding binding;
    private TextView add_new_vocabulary_title_tv;

    public AddNewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new, container, false);
        Button add_new_next_btn = (Button) view.findViewById(R.id.add_new_next_btn);
        add_new_vocabulary_title_tv = (TextView) view.findViewById(R.id.add_new_tv);

        add_new_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AddNewFragmentPt2());
                ft.commit();
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

        return view;
    }
}