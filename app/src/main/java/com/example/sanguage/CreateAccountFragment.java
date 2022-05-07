package com.example.sanguage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CreateAccountFragment extends Fragment {

    private Button create_account;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        create_account = (Button) getView().findViewById(R.id.create_account_btn);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tu chyba nie tak powinno być

                //Intent intent = new Intent(getContext(), SignUpActivity.class);
                //startActivity(intent);
            }
        });

        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }
}