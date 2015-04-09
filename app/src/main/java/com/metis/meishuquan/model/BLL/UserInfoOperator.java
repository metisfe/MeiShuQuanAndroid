package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ on 2015/4/9.
 */
public class UserInfoOperator {

    private static final String TAG = UserInfoOperator.class.getSimpleName();

    private static UserInfoOperator sOperator = new UserInfoOperator();

    private static String URL_ = "v1.1/UserCenter/GetUser?userId=";

    public static UserInfoOperator getInstance () {
        return sOperator;
    }

    private Map<String, User> mUserCache = new HashMap<String, User>();

    private UserInfoOperator () {

    }

    public void getUserInfo (String uid, final OnGetListener<User> userListener) {
        getUserInfo(uid, false, userListener);
    }

    public void getUserInfo (final String uid, boolean refreshCache, final OnGetListener<User> userListener) {
        if (mUserCache.containsKey(uid)) {
            userListener.onGet(true, mUserCache.get(uid));
            if (!refreshCache) {
                return;
            }
        }
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            ApiDataProvider.getmClient().invokeApi(URL_ + uid, null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String data = jsonObject.getString("data");
                            User user = gson.fromJson(data, User.class);

                            Log.v(TAG, "getUserInfo user=" + user.getName());
                            if (user != null) {
                                mUserCache.put(uid, user);
                                userListener.onGet(true, user);
                            } else {
                                userListener.onGet(false, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        userListener.onGet(false, null);
                    }

                }
            });
            /*if (flag) {
                StringBuilder sb = new StringBuilder(LOGIN);
                sb.append("account=" + account);
                sb.append("&pwd=" + pwd);
                ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), callback);
            }*/
        }
    }

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
