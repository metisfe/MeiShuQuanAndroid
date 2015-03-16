package com.metis.meishuquan;

import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.metis.meishuquan.ui.SelectedTabType;
import com.metis.meishuquan.util.GlobalData;

/**
 * Created by wudi on 3/15/2015.
 */
public class AppStatus {
    private static final String PREFERENCES_NAME = "bing_score_token";
    private static final String PREFERENCES_NAME_LASTPAGE = "bing_score_last_page";

    public static void keepSelectedTabType()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME_LASTPAGE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("tab_bar", GlobalData.getInstance().getTabTypeSelected().ordinal());
        editor.putInt("title_bar", GlobalData.getInstance().getTitleBarTypeSelected());
        editor.commit();
    }

    public static void readSelectedTabType()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME_LASTPAGE, 0);
        int tab_type = pref.getInt("tab_bar", -1);
        SelectedTabType enumType = SelectedTabType.values()[0];
        int title_bar = -1;
        if (tab_type != -1)
        {
            enumType = SelectedTabType.values()[tab_type];
            title_bar = pref.getInt("title_bar", -1);
        }

        GlobalData.getInstance().setTabTypeSelected(enumType);
        GlobalData.getInstance().setTitleBarTypeSelected(title_bar);
    }

    public static void keepIsFistLogin(boolean state)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirst", state);
        editor.commit();
    }

    public static boolean isFirstLogin()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        return pref.getBoolean("isFirst", true);
    }

    private static void clearLastPage()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME_LASTPAGE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static void clear()
    {
        clearLastPage();
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        GlobalData.getInstance().resetTabTypeSelected();
        CookieSyncManager.createInstance(MainApplication.MainActivity);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeAllCookie();
    }

    public static void keepNoImageMode(boolean isNoImageModeOn)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isNoImageMode", isNoImageModeOn);
        editor.commit();
    }

    public static boolean isNoImageModeOn()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        return pref.getBoolean("isNoImageMode", false);
    }

    public static void keepSmallMidImageMode(boolean isSmallMidImageMode)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSmallMidImageMode", isSmallMidImageMode);
        editor.commit();
    }

    public static boolean isSmallMidImageModeOn()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        return pref.getBoolean("isSmallMidImageMode", true);
    }

    public static void keepPushNotificationMode(boolean isSmallMidImageMode)
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isPushNotificationMode", isSmallMidImageMode);
        editor.commit();
    }

    public static boolean isPushNotificationModeOn()
    {
        SharedPreferences pref = MainApplication.UIContext.getSharedPreferences(PREFERENCES_NAME, 0);
        return pref.getBoolean("isPushNotificationMode", true);
    }
}
