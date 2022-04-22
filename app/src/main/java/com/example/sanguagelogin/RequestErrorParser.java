package com.example.sanguagelogin;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestErrorParser {
    public static String parseError(VolleyError error) throws JSONException {
        String responseBody = new String(error.networkResponse.data);
        JSONObject data = new JSONObject(responseBody);
        String message = data.getString("message");
        return message;
    }
}
