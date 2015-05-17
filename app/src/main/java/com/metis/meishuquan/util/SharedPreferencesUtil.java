package com.metis.meishuquan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.metis.meishuquan.MainApplication;

import java.util.Map;

/**
 * Created by WJ on 2015/3/24.
 */
public class SharedPreferencesUtil {
    /*头条*/
    public static final String CHANNELS = "CHANNELS";
    public static final String CHANNELID_NEWS = "NEWS";
    /*点评*/
    public static final String ASSESS_LIST_FILTER_DATA = "ASSESS_LIST_FILTER_DATA";//点评列表筛选条件
    public static final String ALL_ASSESS_LIST = "ALL_ASSESS_LIST";//点评列表数据
    public static final String REGION = "REGION";//省市
    public static final String CHECKED_ASSESS_FILTER = "CHECKED_ASSESS_FILTER";//点评列表已选择的筛选条件
    /*课程*/
    public static final String COURSE_LIST = "COURSE_LIST";//课程列表
    public static final String COURSE_CHANNEL_LIST = "COURSE_CHANNEL_LIST";//课程标签
    public static final String COURSE_IMG_LIST = "COURSE_IMG_LIST";//课程图片列表
    /*我*/
    public static final String USER_ROLE = "USER_ROLE";
    public static final String USER_LOGIN_INFO = "USER_LOGIN_INFO";
    public static final String CHECKED_CHANNEL_ITEMS = "CHECKED_CHANNEL_ITEMS";//已选择的课程类型

    /*系统*/
    public static final String MOMENTS_GROUP_INFO = "MOMENTS_GROUP_INFO";

    /*系统*/
    public static final String LAST_APP_VERSION = "LAST_APP_VERSION";


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
