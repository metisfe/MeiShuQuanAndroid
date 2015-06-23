package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.activity.act.StudentListActivity;
import com.metis.meishuquan.fragment.act.StudentCanceledFragment;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.SystemUtil;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by WJ on 2015/5/4.
 */
public class ActiveOperator {

    private static final String TAG = ActiveOperator.class.getSimpleName();

    private static final String
            URL_ACTIVE_DETAIL = "v1.1/Activity/Detil",
            URL_ACTIVE_STUDIO_LIST = "v1.1/Activity/StudioList",
            URL_ACTIVE_TOP_LIST_BY_STUDENT = "/v1.1/Activity/TopListByStudent",
            URL_ACTIVE_TOP_LIST_BY_STUDIO = "v1.1/Activity/TopListByStudio",
            URL_ACTIVE_CHANGE_STUDIO = "v1.1/Activity/ChangeStudio",
            URL_ACTIVE_JOIN_ACTIVITY = "v1.1/Activity/JoinActivity",
            URL_ACTIVE_SELECT_STUDIO = "v1.1/Activity/SelectStudio",
            URL_ACTIVE_MY_ACTIVE_INFO = "v1.1/Activity/GetMyActivityInfo?activityId=",
            URL_ACTIVE_GET_STUDIO_STUDENT = "v1.1/Activity/GetStudioStudent",
            URL_ACTIVE_GET_CANCEL_STUDIO_STUDENT = "v1.1/Activity/GetCancelStudioStudent?activityId=";

    private static final String
            KEY_SESSION = "session",
            KEY_PROVINCE = "province",
            KEY_CITY = "city",
            KEY_COUNTRY = "county",
            KEY_MAJORS_ID = "majorsid",
            KEY_TYPE = "type",
            KEY_COLLEGE_ID = "collegeId",
            KEY_INDEX = "index",
            KEY_STUDIO_ID = "studioId",
            KEY_REGION = "region",
            KEY_QUERY_CONTENT = "queryContent",
            KEY_ACTIVITY_ID = "activityId",
            KEY_QUERY = "query",
            KEY_LAST_USER_ID = "LastUserid";

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

    public void getMyActiveInfo (int activeId, final UserInfoOperator.OnGetListener<SimpleActiveInfo> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_MY_ACTIVE_INFO);
            sb.append(activeId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            Log.v(TAG, "getMyActiveInfo request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<SimpleActiveInfo> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<SimpleActiveInfo>>() {
                        }.getType());
                        if (listener != null && resultInfo.getOption().getStatus() == 0) {
                            listener.onGet(true, resultInfo.getData());
                        }
                        Log.v(TAG, "getMyActiveInfo resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    /*api/v1.1/Activity/StudioList?activityId={activityId}&province={province}&city={city}&county={county}&type={type}&collegeId={collegeId}&majorsid={majorsid}&index={index}&query={query}*/
    /*province（省份id）,type(1推荐 2最新 3最热 ),collegeId(学院id),  index（0）{activityId} （活动id）city(市id)county（县id），majorsid(专业id)，query（模糊查询机构名称）*/
    public void getStudioList (int provinceId, int cityId, int townId, int activityId, int type, int collegeId, int majorsid, int index, String query, final UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_STUDIO_LIST);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_PROVINCE + "=" + provinceId);
            sb.append("&" + KEY_CITY + "=" + cityId);
            sb.append("&" + KEY_COUNTRY + "=" + townId);
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activityId);
            sb.append("&" + KEY_MAJORS_ID + "=" + majorsid);
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_COLLEGE_ID + "=" + collegeId);
            sb.append("&" + KEY_INDEX + "=" + index); //index not working, all data feedback if 0
            sb.append("&" + KEY_QUERY + "=" + URLEncoder.encode(query));
            Log.v(TAG, "getStudioList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<TopListItem>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<TopListItem>>>(){}.getType());
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, resultInfo.getData());
                        }
                        Log.v(TAG, "getStudioList resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void selectStudio (long studioId, int activityId, final UserInfoOperator.OnGetListener<Result> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_SELECT_STUDIO);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_STUDIO_ID + "=" + studioId);
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activityId);
            Log.v(TAG, "selectStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result resultInfo = gson.fromJson(resultJson, Result.class);
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, null);
                        }
                        Log.v(TAG, "selectStudio resultJson=" + resultJson);
                    }
                }
            });
        }
    }
    //URL_ACTIVE_TOP_LIST_BY_STUDENT
    public void topListByStudent (int type, int region, int index, String queryContent, final UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_TOP_LIST_BY_STUDENT);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_REGION + "=" + region);
            sb.append("&" + KEY_INDEX + "=" + index);
            //if (!TextUtils.isEmpty(queryContent)) {
            sb.append("&" + KEY_QUERY_CONTENT + "=" + URLEncoder.encode(queryContent));
            //}
            Log.v(TAG, "topListByStudent request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<TopListItem>> resultData = gson.fromJson(resultJson, new TypeToken<Result<List<TopListItem>>>(){}.getType());
                        if (listener != null && resultData.getOption().getStatus() == 0) {
                            listener.onGet(true, resultData.getData());
                        }
                        Log.v(TAG, "topListByStudent resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void topListByStudio (int type, int region, int index, String queryContent, final UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_TOP_LIST_BY_STUDIO);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_TYPE + "=" + type);
            sb.append("&" + KEY_REGION + "=" + region);
            sb.append("&" + KEY_INDEX + "=" + index);
            //if (!TextUtils.isEmpty(queryContent)) {
                sb.append("&" + KEY_QUERY_CONTENT + "=" + URLEncoder.encode(queryContent));
            //}
            Log.v(TAG, "topListByStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<TopListItem>> resultData = gson.fromJson(resultJson, new TypeToken<Result<List<TopListItem>>>(){}.getType());
                        if (listener != null && resultData.getOption().getStatus() == 0) {
                            listener.onGet(true, resultData.getData());
                        }
                        Log.v(TAG, "topListByStudio resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void changeStudio (long studioId, int activityId, final UserInfoOperator.OnGetListener<Result> listener) {
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
                        Result resultInfo = gson.fromJson(resultJson, Result.class);
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, null);
                        }
                        Log.v(TAG, "getActiveDetail resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void joinActivity (int activityId, final UserInfoOperator.OnGetListener listener) {
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
                        Result resultInfo = gson.fromJson(resultJson, new TypeToken<Result>(){}.getType());
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, null);
                        }
                        Log.v(TAG, "joinActivity resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getStudioStudent (final int activeId, long lastUserId, final UserInfoOperator.OnGetListener<List<StudentListActivity.Student>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_GET_STUDIO_STUDENT);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activeId);
            sb.append("&" + KEY_LAST_USER_ID + "=" + lastUserId);
            Log.v(TAG, "getStudioStudent request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<StudentListActivity.Student>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<StudentListActivity.Student>>>(){}.getType());
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, resultInfo.getData());
                        }
                        Log.v(TAG, "getStudioStudent resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public void getCancelStudioStudent (final int activeId, final UserInfoOperator.OnGetListener<List<StudentCanceledFragment.CanceledStudent>> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_GET_CANCEL_STUDIO_STUDENT);
            sb.append(activeId);
            sb.append("&" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());

            Log.v(TAG, "getCancelStudioStudent request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    if (result != null) {
                        Gson gson = new Gson();
                        String resultJson = gson.toJson(result);
                        Result<List<StudentCanceledFragment.CanceledStudent>> resultInfo = gson.fromJson(resultJson, new TypeToken<Result<List<StudentCanceledFragment.CanceledStudent>>>(){}.getType());
                        if (listener != null) {
                            listener.onGet(resultInfo.getOption().getStatus() == 0, resultInfo.getData());
                        }
                        Log.v(TAG, "getCancelStudioStudent resultJson=" + resultJson);
                    }
                }
            });
        }
    }

    public static class SimpleActiveInfo {
        public int pId;
        public long userId;
        public long studioId;
        public String createDatetime;
        public int activityId;
        public boolean isJoin;
        public int updateCount;
        public int upCount;
        public int totalUpCount;


        public int getpId() {
            return pId;
        }

        public void setpId(int pId) {
            this.pId = pId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public boolean isJoin() {
            return isJoin;
        }

        public void setIsJoin(boolean isJoin) {
            this.isJoin = isJoin;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public void setUpdateCount(int updateCount) {
            this.updateCount = updateCount;
        }

        public long getStudioId() {
            return studioId;
        }

        public void setStudioId(long studioId) {
            this.studioId = studioId;
        }

        public int getUpCount() {
            return upCount;
        }

        public void setUpCount(int upCount) {
            this.upCount = upCount;
        }

        public int getTotalUpCount() {
            return totalUpCount;
        }

        public void setTotalUpCount(int totalUpCount) {
            this.totalUpCount = totalUpCount;
        }
    }
}