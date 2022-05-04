package com.example.sanguage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;


public class ProfileFragment extends Fragment {
    private TextInputEditText profile_change_username_et;
    private TextInputEditText profile_change_password_et;
    private Button profile_save_btn;
    private Context context;
    private Long userID;
    private RequestQueue requestQueue;

    public enum RequestOption {USERNAME, PASSWORD, BOTH}

    public ProfileFragment(Long userID) {
        this.userID = userID;
    }

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void disableChangeDataActions() {
        profile_change_username_et.setEnabled(false);
        profile_change_password_et.setEnabled(false);
        profile_save_btn.setEnabled(false);
    }

    public void enableChangeDataActions() {
        profile_change_username_et.setEnabled(true);
        profile_change_password_et.setEnabled(true);
        profile_save_btn.setEnabled(true);
    }

    public void saveBtnHandler() {
        disableChangeDataActions();
        String password = profile_change_password_et.getText().toString();
        String username = profile_change_username_et.getText().toString();
        boolean passwordEmpty = password.isEmpty();
        boolean usernameEmpty = username.isEmpty();
        if (!passwordEmpty && usernameEmpty) {
            if (!Utils.validatePassword(password)) {
                Toast.makeText(context, "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                enableChangeDataActions();
            } else {
                changeDataRequest(password, RequestOption.PASSWORD);
            }

        } else if (!usernameEmpty && passwordEmpty) {
            changeDataRequest(username, RequestOption.USERNAME);
        } else if (!passwordEmpty && !usernameEmpty) {
            if (!Utils.validatePassword(password)) {
                Toast.makeText(context, "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                enableChangeDataActions();
            } else {
                changeDataRequest(password, RequestOption.BOTH);
            }
        }
        else{
            enableChangeDataActions();
        }
    }

    public void changeDataRequest(String data, RequestOption option) {
        String changeDataURL = "";
        if (option.equals(RequestOption.PASSWORD) || option.equals(RequestOption.BOTH)) {
            changeDataURL = "https://sanguage.herokuapp.com/user/passChange?userID=" + userID + "&password=" + data;
        } else if (option.equals(RequestOption.USERNAME)) {
            changeDataURL = "https://sanguage.herokuapp.com/user/usernameChange?userID=" + userID + "&username=" + data;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, changeDataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                if (option.equals(RequestOption.BOTH)) {
                    changeDataRequest(profile_change_username_et.getText().toString(), RequestOption.USERNAME);
                }
                enableChangeDataActions();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                enableChangeDataActions();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        requestQueue = Volley.newRequestQueue(context);
        profile_change_password_et = view.findViewById(R.id.profile_change_password_et);
        profile_change_username_et = view.findViewById(R.id.profile_change_username_et);
        profile_save_btn = view.findViewById(R.id.profile_save_btn);

        profile_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtnHandler();
            }
        });
        return view;
    }
}