package com.example.sanguage.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static TranslateAnimation shakeError(int fromXDelta, int toXDelta, int fromYDelta, int toYDelta, int duration, int cycles) {
        TranslateAnimation shake = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        shake.setDuration(duration);
        shake.setInterpolator(new CycleInterpolator(cycles));
        return shake;
    }

    public static boolean validateEmail(String email) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validatePassword(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
