package com.metis.meishuquan;

import android.content.SharedPreferences;

/**
 * Created by wudi on 3/15/2015.
 */
public class UserGuide {
    private static final String PREFERENCES_NAME = "meishuquan_guide";

    public static void clear()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean isFirstTo(String prefName)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        return pref.getBoolean(prefName, true);
    }

    public static void setIsFirstTo(String prefName, boolean state)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(prefName, state);
        editor.commit();
    }
}
