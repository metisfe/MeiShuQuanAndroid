package com.metis.meishuquan.model.BLL;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.model.assess.City;
import com.metis.meishuquan.model.commons.College;
import com.metis.meishuquan.model.commons.Comment;
import com.metis.meishuquan.model.commons.InviteCodeInfo;
import com.metis.meishuquan.model.commons.Item;
import com.metis.meishuquan.model.commons.MyFriendList;
import com.metis.meishuquan.model.commons.Option;
import com.metis.meishuquan.model.commons.Profile;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.School;
import com.metis.meishuquan.model.commons.Studio;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.PatternUtils;
import com.metis.meishuquan.util.SystemUtil;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
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
                            URL_FEEDBACK = "v1.1/Instrument/FeedBack",
                            URL_SEARCH_STUDIO = "v1.1/UserCenter/StudioList?query=",
                            URL_PROVINCE = "v1.1/UserCenter/Province",
                            URL_SCHOOL = "v1.1/UserCenter/SchoolList?query=",
                            URL_STUDIO = "v1.1/UserCenter/StudioList?query=",
                            URL_COLLEGE = "v1.1/UserCenter/CollegeList?query=",
                            URL_GET_AREA_LIST = "v1.1/UserCenter/ProvinceLink?id=",
                            URL_GET_MY_FRIENDS = "v1.1/Message/MyFriendList?type=",
                            URL_GET_MY_PHOTOS = "v1.1/UserCenter/MyPhotos?userid=",
                            URL_GET_PROVINCE_STUDIO = "v1.1/UserCenter/GetProvincStudioCount?id=",
                            URL_GET_INVITE_CODE = "v1.1/UserCenter/GetMyInvitationCode",
                            URL_GET_MY_INVITED_ONES = "v1.1/UserCenter/GetMyInvitionUser";

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
                        KEY_CREATE_TIME = "Createtime",
                        KEY_LAST_ID = "lastid";

    public static UserInfoOperator getInstance () {
        return sOperator;
    }

    private Map<String, User> mUserCache = new HashMap<String, User>();

    private UserInfoOperator () {

    }

    public void getMyFriends (int type, final OnGetListener<MyFriendList> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_MY_FRIENDS);
            sb.append(type);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getMyFriends request " + sb);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, " getMyFriends json=" + json);
                        Result<MyFriendList> resultData = gson.fromJson(json, new TypeToken<Result<MyFriendList>>() {
                        }.getType());
                        if (resultData != null) {
                            MyFriendList list = resultData.getData();
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

    public void updateUserProfile (final long userId, Bitmap bmp) {
        byte[] data = ImageLoaderUtils.BitmapToByteArray(bmp);
        String defineStr = data.length + "," + 1 + "," + data.length;
        AssessOperator.getInstance().fileUpload(FileUploadTypeEnum.IMG, defineStr, data, new ServiceFilterResponseCallback() {

            @Override
            public void onResponse(ServiceFilterResponse response, Exception exception) {
                if (response != null) {
                    String result = response.getContent();
                    Log.v(TAG, "updateUserProfile " + result);
                    Gson gson = new Gson();
                    Result<List<Profile>> profileResult = gson.fromJson(result, new TypeToken<Result<List<Profile>>>() {
                    }.getType());
                    if (profileResult.getOption().getStatus() == 0) {
                        updateUserProfileByUrl(userId, profileResult.getData().get(0).getOriginalImage());
                    }
                    /*if (listener != null) {
                        listener.onGet(true, profileResult.getData());
                    }*/
                }
            }
        });
    }

    public void updateUserProfile (final long userId, String path) {
        Bitmap bmp = null;
        try {
            bmp = Bimp.getInstance().revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmp == null) {
            return;
        }
        updateUserProfile(userId, bmp);
    }

    public void updateUserProfileByUrl (long userId, String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(User.KEY_USERAVATAR, url);
        updateUserInfo(userId, map);
    }

    public void updateUserCover (final long userId, Bitmap bmp) {
        byte[] data = ImageLoaderUtils.BitmapToByteArray(bmp);
        String defineStr = data.length + "," + 1 + "," + data.length;
        AssessOperator.getInstance().fileUpload(FileUploadTypeEnum.IMG, defineStr, data, new ServiceFilterResponseCallback() {

            @Override
            public void onResponse(ServiceFilterResponse response, Exception exception) {
                if (response != null) {
                    String result = response.getContent();
                    Log.v(TAG, "updateUserProfile " + result);
                    Gson gson = new Gson();
                    Result<List<Profile>> profileResult = gson.fromJson(result, new TypeToken<Result<List<Profile>>>() {
                    }.getType());
                    if (profileResult.getOption().getStatus() == 0) {
                        updateUserCoverByUrl(userId, profileResult.getData().get(0).getOriginalImage());
                    }
                    /*if (listener != null) {
                        listener.onGet(true, profileResult.getData());
                    }*/
                }
            }
        });
    }

    public void updateUserCover (final long userId, String path) {
        Bitmap bmp = null;
        try {
            bmp = Bimp.getInstance().revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmp == null) {
            return;
        }
        updateUserCover(userId, bmp);
    }

    public void updateUserCoverByUrl (long userId, String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(User.KEY_BACKGROUND_IMG, url);
        updateUserInfo(userId, map);
    }

    public void updateUserInfo (long uid, Map<String, String> map) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            JsonObject json = new JsonObject();
            json.addProperty(KEY_USER_ID, uid);
            Set<String> set = map.keySet();
            for (String key : set) {
                String value = map.get(key);
                value = URLEncoder.encode(value);
                /*if (!Patterns.WEB_URL.matcher(value).matches()) {
                    value = URLEncoder.encode(value);
                }*/
                json.addProperty(key, value);
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
                        if (resultData != null && resultData.getOption().getStatus() == 0) {
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

    /*Assess = 1,
    AssessComment = 2,
    News = 3,
    NewsComment = 4,
    Course = 5,
    CourseComment = 6,
    Circle = 7,
    CircleComment = 8,
    ActivityStudio = 9,
    ActivityStudent = 10,*/


    public void getFavoriteList (String uid, final int index, final OnGetListener<List<Item>> listener) {
        getFavoriteList(uid, index, 3, listener);
    }

    public void getCourseList (String uid, final int index, final OnGetListener<List<Item>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_FAVORITE);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_INDEX + "=" + index);
            sb.append("&sourcetype" + "=" + 4);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getFavoriteList request " + sb);
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

    public void getMyPhoto (long uid, final OnGetListener<List<Profile>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_MY_PHOTOS);
            sb.append(uid);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getMyPhoto request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getMyPhoto result=" + json);
                        Result<List<Profile>> listResult = gson.fromJson(json, new TypeToken<Result<List<Profile>>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getFavoriteList (String uid, final int index, int type, final OnGetListener<List<Item>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_FAVORITE);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_INDEX + "=" + index);
            sb.append("&sourcetype" + "=" + type);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getFavoriteList request " + sb);
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

    /*0:news 1:comment 2：点评 3：点评评论  4：课程  5：课程评论 6:圈子*/
    public void getCommentsList (String uid, final int index, final OnGetListener<List<Item>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_FAVORITE);
            sb.append(KEY_USER_ID + "=" + uid);
            sb.append("&" + KEY_INDEX + "=" + index);
            sb.append("&sourcetype" + "=" + 3);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getCommentsList request " + sb);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, index + " getCommentsList json=" + json);
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

    public void getQuestionList (long uid, int index, int type, final OnGetListener<List<Assess>> listener) {
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
                        Result<List<Assess>> listResult = gson.fromJson(json, new TypeToken<Result<List<Assess>>>(){}.getType());
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
            Bitmap bmp = null;
            try {
                bmp = Bimp.getInstance().revitionImageSize(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            byte[] bytes = os.toByteArray();
            String defineStr = bytes.length + "," + 1 + "," + bytes.length;

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
            jsonObject.addProperty(KEY_USER_ID, MainApplication.userInfo.getUserId());
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
            /*sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());*/
            Log.v(TAG, "feedback request=" + sb.toString() + " and params=" + jsonObject);
            ApiDataProvider.getmClient().invokeApi(sb.toString(), jsonObject, HttpPost.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

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

    public void searchDepartment (String key, final OnGetListener<List<User>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_SEARCH_STUDIO);
            sb.append(URLEncoder.encode(key));
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "searchDepartment request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "searchDepartment result=" + json);
                        Result<List<User>> listResult = gson.fromJson(json, new TypeToken<Result<List<User>>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void searchSchool (String key, final OnGetListener<List<School>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_SCHOOL);
            sb.append(URLEncoder.encode(key));
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "searchSchool request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "searchSchool result=" + json);
                        Result<List<School>> listResult = gson.fromJson(json, new TypeToken<Result<List<School>>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void searchStudio (String key, final OnGetListener<List<Studio>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO);
            sb.append(URLEncoder.encode(key));
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "searchStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "searchStudio result=" + json);
                        Result<List<Studio>> listResult = gson.fromJson(json, new TypeToken<Result<List<Studio>>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getCollegeList (String query, final OnGetListener<List<College>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_COLLEGE);
            sb.append(URLEncoder.encode(query));
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getCollegeList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getCollegeList result=" + json);
                        Result<List<College>> listResult = gson.fromJson(json, new TypeToken<Result<List<College>>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    private static final String SHARE_PROVINCE_LIST = TAG + "_province_list";
    private List<SimpleProvince> mProvinceList = null;

    public String getProvinceName (int id) {
        final SharedPreferences shared = MainApplication.UIContext.getSharedPreferences(SHARE_PROVINCE_LIST, Context.MODE_PRIVATE);
        String json = shared.getString(SHARE_PROVINCE_LIST, null);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            Result<List<SimpleProvince>> listResult = gson.fromJson(json, new TypeToken<Result<List<SimpleProvince>>>(){}.getType());
            mProvinceList = listResult.getData();
        }
        if (mProvinceList == null || mProvinceList.isEmpty()) {
            getProvinceList(null);
        }
        if (mProvinceList == null) {
            return null;
        }
        for (SimpleProvince province : mProvinceList) {
            if (province.provinceId == id) {
                return province.getName();
            }
        }
        return null;
    }

    public void getAreaList (final int id, final OnGetListener<List<City>> listener) {
        /*final SharedPreferences shared = MainApplication.UIContext.getSharedPreferences(SHARE_PROVINCE_LIST, Context.MODE_PRIVATE);
        String json = shared.getString(id + "", null);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            Result<List<City>> listResult = gson.fromJson(json, new TypeToken<Result<List<City>>>(){}.getType());
            if (listener != null) {
                if (listResult.getOption().getStatus() == 0) {
                    listener.onGet(true, listResult.getData());
                } else {
                    listener.onGet(false, null);
                }
            }
            return;
        }*/
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_AREA_LIST);
            sb.append(id + "");
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getAreaList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getAreaList result=" + json);
                        Result<List<City>> listResult = gson.fromJson(json, new TypeToken<Result<List<City>>>(){}.getType());
                        if (listResult.getOption().getStatus() == 0) {
                            /*SharedPreferences.Editor editor = shared.edit();
                            editor.putString(id + "", json);
                            editor.commit();*/
                        }
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getInviteCode (final OnGetListener<InviteCodeInfo> listener) {

        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_INVITE_CODE);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getInviteCode request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (response != null) {
                        Log.v(TAG, "getInviteCode callback=" + response.getContent());
                    }
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getInviteCode result=" + json);
                        Result<InviteCodeInfo> listResult = gson.fromJson(json, new TypeToken<Result<InviteCodeInfo>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getMyInvitedOnes (long lastId, final OnGetListener<InviteCodeInfo> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_MY_INVITED_ONES);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_LAST_ID + "=" + lastId);
            Log.v(TAG, "getInviteCode request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getInviteCode result=" + json);
                        Result<InviteCodeInfo> listResult = gson.fromJson(json, new TypeToken<Result<InviteCodeInfo>>(){}.getType());
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getProvinceStudio (final int id, final OnGetListener<List<City>> listener) {

        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_GET_PROVINCE_STUDIO);
            sb.append(id + "");
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getProvinceStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.v(TAG, "getProvinceStudio result=" + json);
                        Result<List<City>> listResult = gson.fromJson(json, new TypeToken<Result<List<City>>>(){}.getType());
                        if (listResult.getOption().getStatus() == 0) {
                            /*SharedPreferences.Editor editor = shared.edit();
                            editor.putString(id + "", json);
                            editor.commit();*/
                        }
                        if (listener != null) {
                            if (listResult.getOption().getStatus() == 0) {
                                listener.onGet(true, listResult.getData());
                            } else {
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

    public void getProvinceList (final OnGetListener<List<SimpleProvince>> listener) {
        if (mProvinceList != null) {
            if (listener != null) {
                listener.onGet(true, mProvinceList);
            }
            return;
        }
        final SharedPreferences shared = MainApplication.UIContext.getSharedPreferences(SHARE_PROVINCE_LIST, Context.MODE_PRIVATE);
        String json = shared.getString(SHARE_PROVINCE_LIST, null);
        Log.v(TAG, "getProvinceList localjson=" + json);
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            Result<List<SimpleProvince>> listResult = gson.fromJson(json, new TypeToken<Result<List<SimpleProvince>>>(){}.getType());
            mProvinceList = listResult.getData();
            if (listener != null) {
                listener.onGet(true, listResult.getData());
            }
            return;
        }
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_PROVINCE);
            //sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getProvinceList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.v(TAG, "getProvinceList result=" + json);
                    Result<List<SimpleProvince>> listResult = gson.fromJson(json, new TypeToken<Result<List<SimpleProvince>>>(){}.getType());
                    if (listResult.getOption().getStatus() == 0) {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString(SHARE_PROVINCE_LIST, json);
                        editor.commit();
                    }

                    mProvinceList = listResult.getData();
                    if (listener != null) {
                        if (listResult.getOption().getStatus() == 0) {
                            listener.onGet(true, listResult.getData());
                        } else {
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

    public static class SimpleProvince implements Serializable{

        private static SimpleProvince sProvince = null;

        int provinceId;
        String name;

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public synchronized static SimpleProvince getDefaultOne (Context context) {
            if (sProvince == null) {
                sProvince = new SimpleProvince();
                sProvince.provinceId = 0;
                sProvince.name = context.getString(R.string.all);
            }
            return sProvince;
        }
    }

    public interface OnGetListener<T> {
        public void onGet (boolean succeed, T t);
    }
}
