package com.metis.meishuquan.push;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by WJ on 2015/6/1.
 */
public class UnReadManager extends AbsManager{

    private static final String TAG = UnReadManager.class.getSimpleName();

    public static final String TAG_NEW_STUDENT = "new_student";

    private static UnReadManager sManager = null;

    private SharedPreferences mPreference = null;

    private Map<String, Integer> mTagCountMap = new HashMap<String, Integer>();
    private Map<String, List<Observable>> mObservableMap = new HashMap<String, List<Observable>>();

    public static synchronized UnReadManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new UnReadManager(context.getApplicationContext());
        }
        return sManager;
    }

    private UnReadManager(Context context) {
        super(context);
        mPreference = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        init();
    }

    public int getCountByTag (String tag) {
        if (mTagCountMap.containsKey(tag)) {
            return mTagCountMap.get(tag).intValue();
        }
        return 0;
    }

    public void notifyByTag (String tag, int count) {
        notifyByTag(tag, count, false);
    }

    /**
     * @param tag the tag you want to change
     * @param count count of change or change to
     * @param increase add count to old count if true
     */
    public void notifyByTag (String tag, int count, boolean increase) {
        int lastCount = 0;
        if (mTagCountMap.containsKey(tag)) {
            lastCount = mTagCountMap.get(tag);
        }

        if (increase) {
            count = lastCount + count;
        }
        mTagCountMap.put(tag, count);
        notifyObservables(tag, count, count - lastCount);
        commit();
    }

    private void notifyObservables (String tag, int count, int delta) {
        List<Observable> list = mObservableMap.get(tag);
        if (list != null && !list.isEmpty()) {
            for (Observable observable : list) {
                observable.onChanged(tag, count, delta);
            }
        }
    }

    public void registerObservable (String tag, Observable observable) {
        List<Observable> list = null;
        if (mObservableMap.containsKey(tag)) {
            list = mObservableMap.get(tag);
        } else {
            list = new ArrayList<Observable>();
            mObservableMap.put(tag, list);
        }
        if (!list.contains(observable)) {
            list.add(observable);
        }
    }

    public void unregisterObservable (String tag, Observable observable) {
        if (mObservableMap.containsKey(tag)) {
            mObservableMap.get(tag).remove(observable);
        }
    }

    public void unregisterAllObservable (String tag) {
        mObservableMap.remove(tag);
    }

    public void unregisterAllObservable () {
        mObservableMap.clear();
    }

    private void init () {
        String jsonString = mPreference.getString(MainApplication.userInfo.getUserId() + "", "");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                mTagCountMap.put(key, jsonObject.getInt(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void commit () {
        JsonObject jsonObject = new JsonObject();
        Set<String> set = mTagCountMap.keySet();
        for (String key : set) {
            jsonObject.addProperty(key, mTagCountMap.get(key));
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(MainApplication.userInfo.getUserId() + "", jsonObject.toString());
        editor.commit();
    }

    public static interface Observable {
        public void onChanged (String tag, int count, int delta);
    }

}
