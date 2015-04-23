package com.metis.meishuquan.model.BLL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.util.IOUtils;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.Comment;
import com.metis.meishuquan.model.commons.Item;
import com.metis.meishuquan.model.commons.Option;
import com.metis.meishuquan.model.commons.Profile;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                            URL_QUESTION = "v1.1/UserCenter/MyQuestion?",
                            URL_UPDATE_USER_INFO = "v1.1/UserCenter/UpdateUserInfo?param=",
                            URL_CHANGE_PWD = "v1.1/UserCenter/ChangePassword?",
                            URL_FEEDBACK = "v1.1/Instrument/FeedBack";

    private static String KEY_USER_ID = "userId",
                        KEY_INDEX = "index",
                        KEY_SESSION = "session",
                        KEY_OLD_PWD = "oldPwd",
                        KEY_NEW_PWD = "newPwd",
                        KEY_TYPE = "type",
                        KEY_APPVERSION = "AppVersion",
                        KEY_PHONE_VERSION = "PhoneVersion",
                        KEY_CURRENT_IP = "CurrentIP",
                        KEY_CURRENT_REGION = "CurrentRegion",
                        KEY_NET_WORK = "NetWork",
                        KEY_FEED_MESSAGE = "FeedMessage",
                        KEY_FEED_IMAGE = "FeedImage",
                        KEY_CREATE_TIME = "Createtime";

    public static UserInfoOperator getInstance () {
        return sOperator;
    }

    private Map<String, User> mUserCache = new HashMap<String, User>();

    private UserInfoOperator () {

    }

    public void updateUserProfile (final long userId, Bitmap bmp) {
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

    public void updateUserProfile (final long userId, String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        updateUserProfile(userId, bmp);
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
                        Log.v(TAG, "updateUserInfo failed " + exception.getMessage() + " " + response.getContent());
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
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
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

    public void changePwd (String old, String newPwd, final OnGetListener<Option> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_CHANGE_PWD);
            sb.append(KEY_OLD_PWD + "=" + old);
            sb.append("&" + KEY_NEW_PWD + "=" + newPwd);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "changePwd request " + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Result r = gson.fromJson(json, Result.class);
                        listener.onGet(true, r.getOption());
                        Toast.makeText(MainApplication.UIContext, "changePwd status=" + r.getOption().getStatus(), Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "changePwd result " + json + " statusCode=" + response.getStatus().getStatusCode() + " option.status=" + r.getOption().getStatus());
                    }
                }
            });
        }

    }

    public void getQuestionList (long uid, int index, int type, final OnGetListener<List<Comment>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_QUESTION);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_INDEX + "=" + index);
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getQuestionList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getQuestionList result=" + json);
                        Result<List<Comment>> listResult = gson.fromJson(json, new TypeToken<Result<List<Comment>>>(){}.getType());
                        if (listResult.getOption().getStatus() == 0) {
                            listener.onGet(true, listResult.getData());
                        } else {
                            listener.onGet(false, null);
                        }

                    } else {
                        listener.onGet(false, null);
                    }

                }
            });
        }
    }

    /*public void getCourseList (String uid, int index, int type, OnGetListener<>) {

    }*/

    public void feedback (final String message, String imagePath) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            if (TextUtils.isEmpty(imagePath)) {
                feedbackWithUrl(message, null);
                return;
            }
            Log.v(TAG, "feedback upload image start ... " + imagePath);
            File file = new File (imagePath);
            String defineStr = file.length() + "," + 1 + "," + file.length();
            byte[] bytes = new byte[(int)file.length()];
            try {
                FileInputStream fis = new FileInputStream(file);
                fis.read(bytes);
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes.length > 0) {
                AssessOperator.getInstance().fileUpload(FileUploadTypeEnum.IMG, defineStr, bytes, new ServiceFilterResponseCallback () {

                    @Override
                    public void onResponse(ServiceFilterResponse response, Exception exception) {
                        Log.v(TAG, "feedback upload image end ... success ? " + (response != null));
                        if (response != null) {
                            String result = response.getContent();
                            Log.v(TAG, "feedback result=" + result);
                            Gson gson = new Gson();
                            Result<List<Profile>> profileResult = gson.fromJson(result, new TypeToken<Result<List<Profile>>>(){}.getType());
                            feedbackWithUrl(message, profileResult.getData().get(0).getOriginalImage());
                    /*if (listener != null) {
                        listener.onGet(true, profileResult.getData());
                    }*/
                        }
                        Log.v(TAG, "onResponse " + response.getContent());
                    }
                });
            }

        }

    }

    public void feedbackWithUrl (String message, String imageUrl) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(KEY_APPVERSION, Utils.getVersion(MainApplication.UIContext));
            jsonObject.addProperty(KEY_PHONE_VERSION, Build.MODEL + "-" + Build.MANUFACTURER + "-" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK);
            jsonObject.addProperty(KEY_FEED_MESSAGE, message);
            //jsonObject.addProperty(KEY_);
            if (!TextUtils.isEmpty(imageUrl)) {
                jsonObject.addProperty(KEY_FEED_IMAGE, imageUrl);
            }
            if (SystemUtil.isConnectedMobile(MainApplication.UIContext)) {
                jsonObject.addProperty(KEY_NET_WORK, "mobile_net_work");
            } else {
                jsonObject.addProperty(KEY_NET_WORK, "wifi_net_work");
            }

            //jsonObject.addProperty(KEY_CURRENT_REGION, Build.);
            jsonObject.addProperty(KEY_CURRENT_IP, Utils.getIpAddress(MainApplication.UIContext));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            jsonObject.addProperty(KEY_CREATE_TIME, format.format(new Date()));
            StringBuilder sb = new StringBuilder(URL_FEEDBACK);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "feedback request=" + sb.toString() + " and params=" + jsonObject);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), jsonObject.toString(), HttpPost.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "feedback result=" + json);
                    }
                }
            });
        }
    }

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
