package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.List;

/**
 * Created by WJ on 2015/5/5.
 */
public class StudioOperator {

    private static final String TAG = StudioOperator.class.getSimpleName();

    private static final String
            URL_STUDIO_BASE_INFO = "v1.1/Studio/StudioBasicInfo?studioId=",
            URL_STUDIO_TEACHER_LIST = "v1.1/Studio/StudioTeachers?studioId=",
            URL_STUDIO_BOOK_LIST = "v1.1/Studio/StudioBooks?studioId=",
            URL_STUDIO_VIDEO_LIST = "v1.1/Studio/StudioVideolist?studioId=",
            URL_STUDIO_ACHIEVEMENT_LIST = "v1.1/Studio/AchievementList?studioId=",
            URL_STUDIO_ACHIEVEMENT_DETAIL = "v1.1/Studio/AchievementDetial?achievementId=",
            URL_STUDIO_WORK_LIST = "v1.1/Studio/StudioPhotos?studioId=";

    private static final String
            KEY_SESSION = "session",
            KEY_STUDIO_ID = "studioId",
            KEY_ACHIEVEMENT_ID = "achievementId",
            KEY_LAST_VIDEO_ID = "lastVideoId",
            KEY_LAST_PHOTO_ID = "lastPhotoId",
            KEY_PHOTO_TYPE = "photoType";

    private static StudioOperator sOperator = new StudioOperator();

    public static StudioOperator getInstance () {
        return sOperator;
    }

    public void getStudioBaseInfo (long studioId, final UserInfoOperator.OnGetListener<StudioBaseInfo> infoOnGetListener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_BASE_INFO);
            sb.append(studioId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getStudioBaseInfo request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "getStudioBaseInfo callback=" + response.getContent());
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<StudioBaseInfo> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<StudioBaseInfo>>(){}.getType());
                        if (infoOnGetListener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                infoOnGetListener.onGet(true, resultInfo.getData());
                            } else {
                                infoOnGetListener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getStudioBaseInfo resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getTeacherList (int studioId, final UserInfoOperator.OnGetListener<List<TeacherInfo>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_TEACHER_LIST);
            sb.append(studioId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getTeacherList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<TeacherInfo>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<TeacherInfo>>>(){}.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getTeacherList resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getBookList (int studioId, final UserInfoOperator.OnGetListener<List<BookInfo>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_BOOK_LIST);
            sb.append(studioId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getBookList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<BookInfo>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<BookInfo>>>(){}.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getBookList resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getVideoList (int studioId, int lastVideoId, final UserInfoOperator.OnGetListener<List<VideoInfo>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_VIDEO_LIST);
            sb.append(studioId);
            sb.append("&" + KEY_LAST_VIDEO_ID + "=" + lastVideoId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getVideoList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<VideoInfo>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<VideoInfo>>>() {
                        }.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getVideoList resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getAchievementList (int studioId, int achievementId, final UserInfoOperator.OnGetListener<List<Achievement>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_ACHIEVEMENT_LIST);
            sb.append(studioId);
            sb.append("&" + KEY_ACHIEVEMENT_ID + "=" + achievementId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getAchievementList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<Achievement>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<Achievement>>>() {
                        }.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getAchievementList resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getAchievementDetail (int achievementId, final UserInfoOperator.OnGetListener<Achievement> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_ACHIEVEMENT_DETAIL);
            sb.append(achievementId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getAchievementDetail request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<Achievement> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<Achievement>>() {
                        }.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getAchievementDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getWorks (long studioId, int lastPhotoId, int photoType, final UserInfoOperator.OnGetListener<List<WorkInfo>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_STUDIO_WORK_LIST);
            sb.append(studioId);
            sb.append("&" + KEY_LAST_PHOTO_ID + "=" + lastPhotoId);
            sb.append("&" + KEY_PHOTO_TYPE + "=" + photoType);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getWorks request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "getWorks callback=" + response.getContent());
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<WorkInfo>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<WorkInfo>>>() {
                        }.getType());
                        if (listener != null) {
                            if (resultInfo.getOption().getStatus() == 0) {
                                listener.onGet(true, resultInfo.getData());
                            } else {
                                listener.onGet(false, null);
                            }
                        }
                        Log.v(TAG, "getWorks resultJson=" + resultJson);
                    }
                }
            });
        }
    }
}
