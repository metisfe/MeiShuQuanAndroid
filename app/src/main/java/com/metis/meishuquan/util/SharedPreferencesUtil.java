package com.metis.meishuquan.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by WJ on 2015/3/24.
 */
public class SharedPreferencesUtil {
    public static final String CHANNELS="channels";
    public static final String CHANNELID_NEWS="news";

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