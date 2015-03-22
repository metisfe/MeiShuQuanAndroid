package com.metis.meishuquan.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wj on 15/3/20.
 */
public class JsonUtils {

    public ArrayList<HashMap<String, Object>> getJSONParserResultArray(
            String JSONString, String[] key) {

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hashMap;
        try {
            JSONObject result = new JSONObject(JSONString)
                    .getJSONObject("NewDataSet");
            JSONArray resultArray = result.getJSONArray("Table");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultTemp = (JSONObject) resultArray.opt(i);
                hashMap = new HashMap<String, Object>();
                for (int j = 0; j < key.length; j++) {
                    if (resultTemp.has(key[j])) {
                        hashMap.put(key[j], resultTemp.get(key[j]));
                    }
                }
                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
