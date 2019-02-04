package com.example.caizongming.oneplusone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by caizongming on 15/6/21.
 */
public class Pref extends PreferenceActivity {

    public static final String PREF = "ONE_PREF";
    public static final String USERNAME = "USERNAME";
    public static final String ICON = "ICON";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }

    public static String getUsername(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(USERNAME, "");
    }

    public static int getIcon(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(ICON, 0);
    }

    public static int getIconResource(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return getIconResourceById(getIcon(context));
    }

    public static int getIconResourceById(int id) {
        int result = 0;
        switch (id) {
            case 0:
                result = R.drawable.man_01;
            break;

            case 1:
                result = R.drawable.man_02;
            break;

            case 2:
                result = R.drawable.man_03;
            break;

            case 3:
                result = R.drawable.man_04;
            break;

            case 4:
                result = R.drawable.man_05;
            break;

            case 5:
                result = R.drawable.woman_01;
            break;

            case 6:
                result = R.drawable.woman_02;
            break;

            case 7:
                result = R.drawable.woman_03;
            break;

            case 8:
                result = R.drawable.woman_04;
            break;

            case 9:
                result = R.drawable.woman_05;
            break;
        }
        return result;
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public static void setIcon(Context context, int id) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(ICON, id);
        editor.commit();
    }

}
