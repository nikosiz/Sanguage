package com.example.sanguage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

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
    private TextView profile_username_tv;
    private TextView profile_amount_nr_tv;

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

    public void dbSizeRequest() {
        String URL = "https://sanguage.herokuapp.com/user/getUserKnownVocabSize?userID=" + userID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String size = response.getString("messages");
                    profile_amount_nr_tv.setText(size);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        requestQueue.add(jsonObjectRequest);
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

    public JSONObject createPassChangeJSON(String oldPassword, String newPassword) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("oldPassword", oldPassword);
        jsonObject.put("newPassword", newPassword);
        return jsonObject;
    }

    public JSONObject createUsernameChangeJSON(String newUsername, String currentPassword) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("newUsername", newUsername);
        jsonObject.put("currentPassword", currentPassword);
        return jsonObject;
    }


        public void changeDataRequest(String data, String currentPassword, RequestOption option) {
        String changeDataURL = "";
        JSONObject jsonObject = null;
        if (option.equals(RequestOption.PASSWORD) || option.equals(RequestOption.BOTH)) {
            changeDataURL=  "https://sanguage.herokuapp.com/user/passChange";
            try {
                jsonObject = createPassChangeJSON(currentPassword, data);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (option.equals(RequestOption.USERNAME)) {
            changeDataURL=  "https://sanguage.herokuapp.com/user/usernameChange";
            try {
                jsonObject = createUsernameChangeJSON(data, currentPassword);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, changeDataURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String messages = response.getString("messages");
                    Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                if(option.equals(RequestOption.USERNAME)){
                    profile_username_tv.setText(data);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username",data);
                    editor.apply();
                }
                if (option.equals(RequestOption.BOTH)) {
                    changeDataRequest(profile_change_username_et.getText().toString(), data, RequestOption.USERNAME);
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
        profile_dark_mode_s.setChecked(darkMode);
        profile_dark_mode_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    compoundButton.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("darkMode", true);
                } else {
                    compoundButton.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("darkMode", false);
                }
                editor.apply();
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
        profile_dark_mode_s = view.findViewById(R.id.profile_dark_mode_s);
        profile_log_out_btn = view.findViewById(R.id.profile_log_out_btn);
        profile_current_password_et = view.findViewById(R.id.profile_current_password_et);
        profile_username_tv = view.findViewById(R.id.profile_username_tv);
        profile_amount_nr_tv = view.findViewById(R.id.profile_amount_nr_tv);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", "username");
        profile_username_tv.setText(username);

        dbSizeRequest();
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