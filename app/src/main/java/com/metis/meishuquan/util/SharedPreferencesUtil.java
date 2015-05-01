package com.metis.meishuquan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.metis.meishuquan.MainApplication;

import java.util.Map;

/**
 * Created by WJ on 2015/3/24.
 */
public class SharedPreferencesUtil {
    public static final String CHANNELS = "CHANNELS";
    public static final String CHANNELID_NEWS = "news";
    public static final String ASSESS_CHANNEL_LIST = "ASSESS_CHANNEL_LIST";
    public static final String REGION = "REGION";
    public static final String ALL_ASSESS_LIST = "ALL_ASSESS_LIST";
    public static final String LOGIN_STATE = "LOGIN_STATE";
    public static final String USER_ROLE = "USER_ROLE";
    public static final String USER_LOGIN_INFO = "USER_LOGIN_INFO";
    public static final String COURSE_LIST = "COURSE_LIST";
    public static final String COURSE_CHANNEL_LIST = "COURSE_CHANNEL_LIST";
    public static final String COURSE_IMG_LIST = "COURSE_IMG_LIST";


    public static final String CHECKED_CHANNEL_ITEMS = "CHECKED_CHANNEL_ITEMS";//已选择的课程类型
    public static final String CHECKED_ASSESS_FILTER = "CHECKED_ASSESS_FILTER";//点评列表筛选条件

    private static SharedPreferencesUtil spu = null;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreferencesUtil(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("MSQ", Context.MODE_PRIVATE);
        this.editor = sp.edit();
    }

    public static SharedPreferencesUtil getInstanse(Context context) {
        if (spu == null && context != null) {
            spu = new SharedPreferencesUtil(context);
        }
        return spu;
    }

    public boolean add(String key, String val) {
        boolean flag = false;
        editor.putString(key, val);
        flag = editor.commit();
        return flag;
    }

    public boolean update(String key, String val) {
        boolean flag = false;
        editor.remove(key);
        editor.putString(key, val);
        flag = editor.commit();
        return flag;
    }

    public boolean delete(String key) {
        boolean flag = false;
        editor.remove(key);
        flag = editor.commit();
        return flag;
    }

    public String getStringByKey(String key) {
        String val = "";
        Map<String, String> map = (Map<String, String>) sp.getAll();
        for (String mkeys : map.keySet()) {
            if (mkeys.equals(key)) {
                val = map.get(key);
            }
        }
        return val;
    }
}
