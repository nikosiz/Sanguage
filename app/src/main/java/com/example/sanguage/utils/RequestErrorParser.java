package com.example.sanguage.utils;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestErrorParser {
    public static String parseError(VolleyError error) throws JSONException {
        byte[] data;
        try {
            data = error.networkResponse.data;
        } catch (NullPointerException n) {

            return "";
        }
        String responseBody = new String(data);
        JSONObject responseData = new JSONObject(responseBody);
        String message = responseData.getString("message");
        return message;
    }
}
