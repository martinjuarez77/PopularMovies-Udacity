package com.martincho.udacity.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by martincho on 26/2/17.
 */

public class PreferencesUtil {

    public static void changeQueryPreferences(String query, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("QUERY", query);
        editor.commit();
    }

}
