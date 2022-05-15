package com.example.sanguage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanguage.utils.RequestErrorParser;
import com.example.sanguage.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {
    private TextInputEditText profile_change_username_et;
    private TextInputEditText profile_change_password_et;
    private TextInputEditText profile_current_password_et;
    private Button profile_save_btn;
    private Button profile_log_out_btn;
    private Button profile_sign_up_btn;
    private Context context;
    private Long userID;
    private RequestQueue requestQueue;
    private Switch profile_dark_mode_s;

    public enum RequestOption {USERNAME, PASSWORD, BOTH}

    public ProfileFragment(Long userID) {
        this.userID = userID;
    }

    public ProfileFragment() {
    }

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

    public void logOutButtonHandler() {
        profile_log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("enabled", false);
                editor.apply();
                Intent intent = new Intent(context, ChooseActivity.class);
                startActivity(intent);
            }
        });
    }

    public void saveButtonHandler() {
        profile_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableChangeDataActions();
                String newPassword = profile_change_password_et.getText().toString();
                String currentPassword = profile_current_password_et.getText().toString();
                String username = profile_change_username_et.getText().toString();
                boolean newPasswordEmpty = newPassword.isEmpty();
                boolean currentPasswordEmpty = currentPassword.isEmpty();
                boolean usernameEmpty = username.isEmpty();
                if (!currentPasswordEmpty) {
                    if (!newPasswordEmpty && usernameEmpty) {
                        if (!Utils.validatePassword(newPassword)) {
                            Toast.makeText(context, "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                            enableChangeDataActions();
                        } else {
                            changeDataRequest(newPassword, currentPassword, RequestOption.PASSWORD);
                        }

                    } else if (!usernameEmpty && newPasswordEmpty) {
                        changeDataRequest(username, currentPassword, RequestOption.USERNAME);
                    } else if (!newPasswordEmpty) {
                        if (!Utils.validatePassword(newPassword)) {
                            Toast.makeText(context, "Password must contain number and special character", Toast.LENGTH_SHORT).show();
                            enableChangeDataActions();
                        } else {
                            changeDataRequest(newPassword, currentPassword, RequestOption.BOTH);
                        }
                    } else {
                        enableChangeDataActions();
                    }
                } else {
                    Toast.makeText(context, "Enter current password", Toast.LENGTH_SHORT).show();
                    profile_current_password_et.startAnimation(Utils.shakeError(5, 10, 0, 0, 500, 7));
                    enableChangeDataActions();
                }
            }
        });
    }

    public void changeDataRequest(String data, String currentPassword, RequestOption option) {
        String changeDataURL = "";
        if (option.equals(RequestOption.PASSWORD) || option.equals(RequestOption.BOTH)) {
            changeDataURL = "https://sanguage.herokuapp.com/user/passChange?userID=" + userID + "&oldPassword=" + data+ "&password="+ currentPassword;
        } else if (option.equals(RequestOption.USERNAME)) {
            changeDataURL = "https://sanguage.herokuapp.com/user/usernameChange?userID=" + userID + "&newUsername=" + data+ "&password="+ currentPassword;
        }
        System.out.println(changeDataURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, changeDataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                if (option.equals(RequestOption.BOTH)) {
                    changeDataRequest(profile_change_username_et.getText().toString(), currentPassword, RequestOption.USERNAME);
                }
                enableChangeDataActions();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                enableChangeDataActions();
                try {
                    String message = RequestErrorParser.parseError(error);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void darkModeHandler() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        boolean darkMode = preferences.getBoolean("darkMode", false);
        if(darkMode){
            profile_dark_mode_s.setChecked(true);
        }else{
            profile_dark_mode_s.setChecked(false);
        }
        profile_dark_mode_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("darkMode", true);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("darkMode", false);
                    editor.apply();
                }
                ((AppWindowAccount) getActivity()).getNav_bar().setItemSelected(R.id.bottom_nav_learn, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        requestQueue = Volley.newRequestQueue(context);
        profile_change_password_et = view.findViewById(R.id.profile_change_password_et);
        profile_change_username_et = view.findViewById(R.id.profile_change_username_et);
        profile_save_btn = view.findViewById(R.id.profile_save_btn);
        profile_dark_mode_s = view.findViewById(R.id.create_account_dark_mode_s);
        profile_log_out_btn = view.findViewById(R.id.profile_log_out_btn);
        profile_current_password_et = view.findViewById(R.id.profile_current_password_et);

        darkModeHandler();
        saveButtonHandler();
        logOutButtonHandler();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}