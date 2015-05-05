package com.metis.meishuquan.model.BLL;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import io.rong.imkit.Res;

/**
 * Created by WJ on 2015/5/4.
 */
public class ActiveOperator {

    private static final String TAG = ActiveOperator.class.getSimpleName();

    private static final String
            URL_ACTIVE_DETAIL = "v1.1/Activity/Detil",
            URL_ACTIVE_STUDIO_LIST = "v1.1/Activity/StudioList",
            URL_ACTIVE_TOP_LIST = "v1.1/Activity/TopListByStudio",
            URL_ACTIVE_CHANGE_STUDIO = "v1.1/Activity/ChangeStudio",
            URL_ACTIVE_JOIN_ACTIVITY = "v1.1/Activity/JoinActivity";

    private static final String
            KEY_SESSION = "session",
            KEY_PROVINCE = "province",
            KEY_TYPE = "type",
            KEY_COLLEGE_ID = "collegeId",
            KEY_INDEX = "index",
            KEY_STUDIO_ID = "studioId",
            KEY_REGION = "region",
            KEY_QUERY_CONTENT = "queryContent",
            KEY_ACTIVITY_ID = "activityId";

    private static ActiveOperator sOperator = new ActiveOperator();

    public static ActiveOperator getInstance () {
        return sOperator;
    }

    public void getActiveDetail(final UserInfoOperator.OnGetListener<ActiveInfo> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_DETAIL);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getActiveDetail request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "getActiveDetail callback " + result + " " + exception + " " + response.getContent());
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<ActiveInfo> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<ActiveInfo>>() {
                        }.getType());
                        if (listener != null && resultInfo.getOption().getStatus() == 0) {
                            listener.onGet(true, resultInfo.getData());
                        }
                        Log.v(TAG, "getActiveDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getStudioList (int provinceId, int type, int collegeId, int index) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_STUDIO_LIST);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_PROVINCE + "=" + provinceId);
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_COLLEGE_ID + "=" + collegeId);
            sb.append("&" + KEY_INDEX + "=" + index);
            Log.v(TAG, "getActiveDetail request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "getActiveDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void selectStudio (int studioId) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_STUDIO_LIST);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_STUDIO_ID + "=" + studioId);
            Log.v(TAG, "getActiveDetail request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "getActiveDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void topListByStudio (int type, int region, int index, String queryContent) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_TOP_LIST);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_REGION + "=" + region);
            sb.append("&" + KEY_INDEX + "=" + index);
            if (!TextUtils.isEmpty(queryContent)) {
                sb.append("&" + KEY_QUERY_CONTENT + "=" + queryContent);
            }
            Log.v(TAG, "topListByStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "topListByStudio callback " + result + " " + exception + " " + response.getContent());
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "topListByStudio resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void changeStudio (int studioId, int activityId) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_CHANGE_STUDIO);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_STUDIO_ID + "=" + studioId);
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activityId);
            Log.v(TAG, "getActiveDetail request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "getActiveDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void joinActivity (int activityId) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_JOIN_ACTIVITY);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activityId);
            Log.v(TAG, "joinActivity request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Log.v(TAG, "joinActivity resultJson=" + resultJson);
                    }
                }
            });
        }
    }
}