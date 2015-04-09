package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.Item;
import com.metis.meishuquan.model.commons.Result;
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
import java.util.List;
import java.util.Map;

/**
 * Created by WJ on 2015/4/9.
 */
public class UserInfoOperator {

    private static final String TAG = UserInfoOperator.class.getSimpleName();

    private static UserInfoOperator sOperator = new UserInfoOperator();

    private static String URL_ = "v1.1/UserCenter/GetUser?userId=",
                            URL_FAVORITE = "v1.1/UserCenter/MyFavorites?userid=100001&index=1";

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
                        Log.v(TAG, "getUserInfo json=" + json);
                        Result<User> resultData = gson.fromJson(json, new TypeToken<Result<User>>(){}.getType());
                        if (resultData != null) {
                            User user = resultData.getData();
                            mUserCache.put(uid, user);

                            if (userListener != null) {
                                userListener.onGet(true, user);
                            }
                        } else {
                            if (userListener != null) {
                                userListener.onGet(false, null);
                            }

                        }

                    } else {
                        if (userListener != null) {
                            userListener.onGet(false, null);
                        }

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

    public void getFavoriteList (String uid, final OnGetListener<List<Item>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            /*StringBuilder sb = new StringBuilder();
            sb.append()*/
            ApiDataProvider.getmClient().invokeApi(URL_FAVORITE, null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getFavoriteList json=" + json);
                        Result<List<Item>> resultData = gson.fromJson(json, new TypeToken<Result<List<Item>>>(){}.getType());
                        if (resultData != null) {
                            List<Item> list = resultData.getData();
                            if (listener != null) {
                                listener.onGet(true, list);
                            }

                        } else {
                            if (listener != null) {
                                listener.onGet(false, null);
                            }

                        }
                    } else {
                        if (listener != null) {
                            listener.onGet(false, null);
                        }

                    }
                }
            });
        }
    }

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
