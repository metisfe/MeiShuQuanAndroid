package com.metis.meishuquan.model.BLL;

import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
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

import io.rong.imkit.Res;

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
            URL_ACTIVE_MY_ACTIVE_INFO = "v1.1/Activity/GetMyActivityInfo?activityId=";

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
            KEY_QUERY = "query";

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
                    //Log.v(TAG, "getActiveDetail callback " + result + " " + exception + " " + response.getContent());
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
                    Log.v(TAG, "getMyActiveInfo callback " + result + " " + exception + " " + response.getContent());
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
            sb.append("&" + KEY_INDEX + "=" + index);
            sb.append("&" + KEY_QUERY + "=" + URLEncoder.encode(query));
            Log.v(TAG, "getStudioList request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "getStudioList callback=" + response.getContent());
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

    public void selectStudio (int studioId, int activityId, final UserInfoOperator.OnGetListener<Result> listener) {
        if (SystemUtil.isNetworkAvailable(MainApplication.UIContext)) {
            StringBuilder sb = new StringBuilder(URL_ACTIVE_SELECT_STUDIO);
            sb.append("?" + KEY_SESSION + "=" + MainApplication.userInfo.getCookie());
            sb.append("&" + KEY_STUDIO_ID + "=" + studioId);
            sb.append("&" + KEY_ACTIVITY_ID + "=" + activityId);
            Log.v(TAG, "selectStudio request=" + sb.toString());
            ApiDataProvider.getmClient().invokeApi(sb.toString(), null, HttpGet.METHOD_NAME, null, (Class<ReturnInfo<String>>) new ReturnInfo<String>().getClass(), new ApiOperationCallback<ReturnInfo<String>>() {

                @Override
                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                    Log.v(TAG, "selectStudio callback=" + response.getContent());
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
                    Log.v(TAG, "topListByStudent callback " + result + " " + exception + " " + response.getContent());
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
                    Log.v(TAG, "topListByStudio callback " + result + " " + exception + " " + response.getContent());
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

    public void changeStudio (int studioId, int activityId, final UserInfoOperator.OnGetListener<Result> listener) {
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

    public static class SimpleActiveInfo {
        public int pId;
        public int userId;
        public int studioId;
        public String createDatetime;
        public int activityId;
        public boolean isJoin;
        public int updateCount;

        public int getpId() {
            return pId;
        }

        public void setpId(int pId) {
            this.pId = pId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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

        public int getStudioId() {
            return studioId;
        }

        public void setStudioId(int studioId) {
            this.studioId = studioId;
        }
    }
}