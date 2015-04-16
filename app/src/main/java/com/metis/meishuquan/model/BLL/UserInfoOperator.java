package com.metis.meishuquan.model.BLL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.Item;
import com.metis.meishuquan.model.commons.Profile;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by WJ on 2015/4/9.
 */
public class UserInfoOperator {

    private static final String TAG = UserInfoOperator.class.getSimpleName();

    private static UserInfoOperator sOperator = new UserInfoOperator();

    private static String URL_CENTER = "v1.1/UserCenter/GetUser?",
                            URL_FAVORITE = "v1.1/UserCenter/MyFavorites?",
                            URL_QUESTION = "v1.1/UserCenter/MyQuestion?userId={userId}&index={index}&type={type}",
                            URL_UPDATE_USER_INFO = "v1.1/UserCenter/UpdateUserInfo?param=";

    private static String KEY_USER_ID = "userId",
                        KEY_INDEX = "index",
                        KEY_SESSION = "session";

    public static UserInfoOperator getInstance () {
        return sOperator;
    }

    private Map<String, User> mUserCache = new HashMap<String, User>();

    private UserInfoOperator () {

    }

    public void updateUserProfile (final long userId, String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        byte[] data = ImageLoaderUtils.BitmapToByteArray(bmp);
        String defineStr = data.length + "," + 1 + "," + data.length;
        AssessOperator.getInstance().fileUpload(FileUploadTypeEnum.IMG, defineStr, data, new ServiceFilterResponseCallback () {

            @Override
            public void onResponse(ServiceFilterResponse response, Exception exception) {
                if (response != null) {
                    String result = response.getContent();
                    Log.v(TAG, "updateUserProfile " + result);
                    Gson gson = new Gson();
                    Result<List<Profile>> profileResult = gson.fromJson(result, new TypeToken<Result<List<Profile>>>(){}.getType());
                    updateUserProfileByUrl(userId, profileResult.getData().get(0).getOriginalImage());
                    /*if (listener != null) {
                        listener.onGet(true, profileResult.getData());
                    }*/
                }
                Log.v(TAG, "onResponse " + response.getContent());
            }
        });
    }

    public void updateUserProfileByUrl (long userId, String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(User.KEY_USERAVATAR, URLEncoder.encode(url));
        updateUserInfo(userId, map);
    }

    public void updateUserInfo (long uid, Map<String, String> map) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            JsonObject json = new JsonObject();
            json.addProperty(KEY_USER_ID, uid);
            Set<String> set = map.keySet();
            for (String key : set) {
                json.addProperty(key, map.get(key));
            }
            StringBuilder sb = new StringBuilder(json.toString());
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "updateUserInfo request url=" + URL_UPDATE_USER_INFO + sb);
            ApiDataProvider.getmClient().invokeApi(URL_UPDATE_USER_INFO + sb, null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "updateUserInfo json=" + json);

                    } else {

                    }

                }
            });
        }
    }

    public void getUserInfo (long uid, final OnGetListener<User> userListener) {
        getUserInfo(uid, false, userListener);
    }

    public void getUserInfo (final long uid, boolean refreshCache, final OnGetListener<User> userListener) {
        if (mUserCache.containsKey(uid)) {
            userListener.onGet(true, mUserCache.get(uid + ""));
            if (!refreshCache) {
                return;
            }
        }
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_CENTER);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getUserInfo request=" + sb);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getUserInfo json=" + json);

                        Result<User> resultData = gson.fromJson(json, new TypeToken<Result<User>>(){}.getType());
                        if (resultData != null) {
                            User user = resultData.getData();
                            mUserCache.put(uid + "", user);

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

    public void getFavoriteList (String uid, final int index, final OnGetListener<List<Item>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_FAVORITE);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_INDEX + "=" + index);
            Log.v(TAG, "before request " + sb);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, index + " getFavoriteList json=" + json);
                        Result<List<Item>> resultData = gson.fromJson(json, new TypeToken<Result<List<Item>>>() {
                        }.getType());
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

    /*public void getQuestionList (String uid, int index, int type, OnGetListener<>) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {

        }
    }*/

    /*public void getCourseList (String uid, int index, int type, OnGetListener<>) {

    }*/

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
